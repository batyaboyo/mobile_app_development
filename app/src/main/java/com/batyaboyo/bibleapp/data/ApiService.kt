package com.batyaboyo.bibleapp.data

import com.batyaboyo.bibleapp.model.Book
import com.batyaboyo.bibleapp.model.Translation
import com.batyaboyo.bibleapp.model.Verse
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class ApiService private constructor(
    private val service: HelloAoService
) : BibleApi {

    constructor() : this(createService())

    override suspend fun fetchTranslations(): List<Translation> = withContext(Dispatchers.IO) {
        parseTranslations(service.getTranslations()).ifEmpty {
            listOf(
                Translation("BSB", "Berean Standard Bible", "BSB"),
                Translation("eng_web", "World English Bible", "WEB")
            )
        }
    }

    override suspend fun fetchBooks(translationId: String): List<Book> = withContext(Dispatchers.IO) {
        parseBooks(service.getBooks(enc(translationId)))
    }

    override suspend fun fetchChapter(translationId: String, bookId: String, chapter: Int): List<Verse> = withContext(Dispatchers.IO) {
        parseChapter(bookId, chapter, service.getChapter(enc(translationId), enc(bookId), chapter))
    }

    private interface HelloAoService {
        @GET("available_translations.json")
        suspend fun getTranslations(): JsonElement

        @GET("{translationId}/books.json")
        suspend fun getBooks(@Path(value = "translationId", encoded = true) translationId: String): JsonElement

        @GET("{translationId}/{bookId}/{chapter}.json")
        suspend fun getChapter(
            @Path(value = "translationId", encoded = true) translationId: String,
            @Path(value = "bookId", encoded = true) bookId: String,
            @Path("chapter") chapter: Int
        ): JsonElement
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }

        internal fun parseTranslations(root: JsonElement): List<Translation> {
            val array = root.asArrayOrNull() ?: root.asObjectOrNull()?.get("translations")?.asArrayOrNull() ?: return emptyList()
            return array.mapNotNull { item ->
                val obj = item.asObjectOrNull() ?: return@mapNotNull null
                val id = obj.string("id").ifBlank { obj.string("short_name") }
                if (id.isBlank()) return@mapNotNull null
                val name = obj.string("name").ifBlank { id }
                val shortName = obj.string("short_name").ifBlank { obj.string("shortName") }.ifBlank { id }
                Translation(id = id, name = name, shortName = shortName)
            }
        }

        internal fun parseBooks(root: JsonElement): List<Book> {
            val array = root.asArrayOrNull() ?: root.asObjectOrNull()?.get("books")?.asArrayOrNull() ?: return emptyList()
            return array.mapIndexedNotNull { index, item ->
                val obj = item.asObjectOrNull() ?: return@mapIndexedNotNull null
                val id = obj.string("id")
                if (id.isBlank()) return@mapIndexedNotNull null
                val name = obj.string("name").ifBlank { obj.string("commonName") }.ifBlank { id }
                val chapters = obj.int("chapters")
                    ?: obj.int("num_chapters")
                    ?: obj.int("numChapters")
                    ?: obj.int("numberOfChapters")
                    ?: 1
                val order = obj.int("order") ?: (index + 1)
                order to Book(id = id, name = name, chapters = chapters)
            }.sortedBy { it.first }.map { it.second }
        }

        internal fun parseChapter(bookId: String, chapter: Int, root: JsonElement): List<Verse> {
            val rootObject = root.asObjectOrNull() ?: return emptyList()
            val chapterContent = rootObject["chapter"]?.asObjectOrNull()?.get("content")?.asArrayOrNull()
            if (chapterContent != null) {
                val parsed = chapterContent.mapNotNull { item ->
                    val obj = item.asObjectOrNull() ?: return@mapNotNull null
                    if (obj.string("type") != "verse") return@mapNotNull null
                    val number = obj.int("number") ?: return@mapNotNull null
                    val text = parseVerseContent(obj["content"])
                    if (text.isBlank()) return@mapNotNull null
                    Verse(number = number, text = text, reference = "${bookId.uppercase()} $chapter:$number")
                }
                if (parsed.isNotEmpty()) return parsed
            }

            val versesArray = rootObject["verses"]?.asArrayOrNull() ?: return emptyList()
            val referencePrefix = "${bookId.uppercase()} $chapter"
            return versesArray.mapIndexedNotNull { index, item ->
                val obj = item.asObjectOrNull() ?: return@mapIndexedNotNull null
                val number = obj.int("verse") ?: obj.int("number") ?: (index + 1)
                val text = obj.string("text").ifBlank { obj.string("content") }.trim()
                if (text.isBlank()) return@mapIndexedNotNull null
                Verse(number = number, text = text, reference = "$referencePrefix:$number")
            }
        }

        internal fun parseVerseContent(content: JsonElement?): String {
            val array = content.asArrayOrNull() ?: return ""
            return buildString {
                array.forEach { part ->
                    when (part) {
                        is JsonPrimitive -> append(part.contentOrNull.orEmpty())
                        is JsonObject -> append(part.string("text"))
                        is JsonArray -> Unit
                    }
                }
            }.trim()
        }

        private fun createService(): HelloAoService {
            return Retrofit.Builder()
                .baseUrl("https://bible.helloao.org/api/")
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(HelloAoService::class.java)
        }

        private fun enc(value: String): String = URLEncoder.encode(value, Charsets.UTF_8.name())

        private fun JsonElement?.asArrayOrNull(): JsonArray? = this as? JsonArray

        private fun JsonElement?.asObjectOrNull(): JsonObject? = this as? JsonObject

        private fun JsonObject.string(key: String): String = (this[key] as? JsonPrimitive)?.contentOrNull.orEmpty()

        private fun JsonObject.int(key: String): Int? = (this[key] as? JsonPrimitive)?.intOrNull?.takeIf { it > 0 }
    }
}
