package com.batyaboyo.bibleapp.data

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ApiServiceParsingTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun parseTranslations_supportsWrappedPayload() {
        val root = json.parseToJsonElement(
            """
            {
              "translations": [
                {"id": "BSB", "name": "Berean Standard Bible", "short_name": "BSB"},
                {"short_name": "WEB", "name": "World English Bible"}
              ]
            }
            """.trimIndent()
        )

        val result = ApiService.parseTranslations(root)

        assertEquals(2, result.size)
        assertEquals("BSB", result.first().id)
        assertEquals("WEB", result.last().id)
    }

    @Test
    fun parseBooks_supportsFallbackFieldsAndOrdering() {
        val root = json.parseToJsonElement(
            """
            {
              "books": [
                {"id": "MAT", "commonName": "Matthew", "numberOfChapters": 28, "order": 40},
                {"id": "GEN", "name": "Genesis", "chapters": 50, "order": 1}
              ]
            }
            """.trimIndent()
        )

        val result = ApiService.parseBooks(root)

        assertEquals(listOf("GEN", "MAT"), result.map { it.id })
        assertEquals(28, result.last().chapters)
    }

    @Test
    fun parseChapter_supportsStructuredChapterContent() {
        val root = json.parseToJsonElement(
            """
            {
              "chapter": {
                "content": [
                  {"type": "heading", "content": ["Intro"]},
                  {"type": "verse", "number": 1, "content": ["In the beginning ", {"text": "God"}, " created."]}
                ]
              }
            }
            """.trimIndent()
        )

        val result = ApiService.parseChapter("GEN", 1, root)

        assertEquals(1, result.size)
        assertEquals("GEN 1:1", result.first().reference)
        assertEquals("In the beginning God created.", result.first().text)
    }

    @Test
    fun parseChapter_supportsLegacyVersesArray() {
        val root = json.parseToJsonElement(
            """
            {
              "verses": [
                {"verse": 1, "text": "For God so loved the world."},
                {"number": 2, "content": "He gave His only Son."}
              ]
            }
            """.trimIndent()
        )

        val result = ApiService.parseChapter("JHN", 3, root)

        assertEquals(2, result.size)
        assertTrue(result.any { it.reference == "JHN 3:1" })
        assertTrue(result.any { it.text == "He gave His only Son." })
    }
}
