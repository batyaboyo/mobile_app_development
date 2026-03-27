package com.theword.app.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// ---- Translations ----

@JsonClass(generateAdapter = false)
data class TranslationsResponse(
    @Json(name = "translations") val translations: List<TranslationDto>? = null
)

@JsonClass(generateAdapter = false)
data class TranslationDto(
    @Json(name = "id") val id: String = "",
    @Json(name = "name") val name: String? = null,
    @Json(name = "englishName") val englishName: String? = null,
    @Json(name = "shortName") val shortName: String? = null,
    @Json(name = "language") val language: String? = null,
    @Json(name = "languageEnglishName") val languageEnglishName: String? = null
)

// ---- Commentaries ----

@JsonClass(generateAdapter = false)
data class CommentariesResponse(
    @Json(name = "commentaries") val commentaries: List<CommentaryDto>? = null
)

@JsonClass(generateAdapter = false)
data class CommentaryDto(
    @Json(name = "id") val id: String = "",
    @Json(name = "name") val name: String? = null,
    @Json(name = "englishName") val englishName: String? = null
)

// ---- Books ----

@JsonClass(generateAdapter = false)
data class BooksResponse(
    @Json(name = "books") val books: List<BookDto>? = null
)

@JsonClass(generateAdapter = false)
data class BookDto(
    @Json(name = "id") val id: String = "",
    @Json(name = "name") val name: String? = null,
    @Json(name = "commonName") val commonName: String? = null,
    @Json(name = "numberOfChapters") val numberOfChapters: Int = 0,
    @Json(name = "order") val order: Int = 0
)

// ---- Chapter ----

@JsonClass(generateAdapter = false)
data class ChapterResponse(
    @Json(name = "chapter") val chapter: ChapterDto? = null,
    @Json(name = "book") val book: BookRefDto? = null,
    @Json(name = "translation") val translation: TranslationRefDto? = null
)

@JsonClass(generateAdapter = false)
data class BookRefDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null
)

@JsonClass(generateAdapter = false)
data class TranslationRefDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "shortName") val shortName: String? = null
)

@JsonClass(generateAdapter = false)
data class ChapterDto(
    @Json(name = "number") val number: Int = 0,
    @Json(name = "content") val content: List<ContentItemDto>? = null,
    @Json(name = "footnotes") val footnotes: List<FootnoteDto>? = null
)

@JsonClass(generateAdapter = false)
data class ContentItemDto(
    @Json(name = "type") val type: String? = null,
    @Json(name = "number") val number: Int? = null,
    @Json(name = "content") val content: Any? = null
)

@JsonClass(generateAdapter = false)
data class FootnoteDto(
    @Json(name = "noteId") val noteId: Int = 0,
    @Json(name = "caller") val caller: String? = null,
    @Json(name = "text") val text: String? = null
)

// ---- Commentary Chapter ----

@JsonClass(generateAdapter = false)
data class CommentaryChapterResponse(
    @Json(name = "chapter") val chapter: CommentaryChapterDto? = null,
    @Json(name = "commentary") val commentary: CommentaryRefDto? = null,
    @Json(name = "book") val book: BookRefDto? = null
)

@JsonClass(generateAdapter = false)
data class CommentaryRefDto(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null
)

@JsonClass(generateAdapter = false)
data class CommentaryChapterDto(
    @Json(name = "number") val number: Int = 0,
    @Json(name = "content") val content: List<ContentItemDto>? = null,
    @Json(name = "introduction") val introduction: String? = null
)
