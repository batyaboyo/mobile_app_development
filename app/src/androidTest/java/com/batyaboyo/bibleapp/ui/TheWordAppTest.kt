package com.batyaboyo.bibleapp.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.batyaboyo.bibleapp.data.AssetRepository
import com.batyaboyo.bibleapp.data.BibleApi
import com.batyaboyo.bibleapp.data.LocalStore
import com.batyaboyo.bibleapp.model.Book
import com.batyaboyo.bibleapp.model.Translation
import com.batyaboyo.bibleapp.model.Verse
import com.batyaboyo.bibleapp.ui.theme.TheWordTheme
import org.junit.Rule
import org.junit.Test

class TheWordAppTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun switchesTranslationAndLoadsBooks() {
        val api = FakeBibleApi(
            translations = listOf(
                Translation("BSB", "Berean Standard Bible", "BSB"),
                Translation("WEB", "World English Bible", "WEB")
            ),
            booksByTranslation = mapOf(
                "BSB" to listOf(Book("GEN", "Genesis", 50)),
                "WEB" to listOf(Book("MAT", "Matthew", 28))
            )
        )

        setAppContent(api)

        composeRule.onNodeWithTag("tab_bible").performClick()
        composeRule.onNodeWithTag("version_selector").performClick()
        composeRule.onNodeWithText("World English Bible (WEB)").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("book_selector").assertTextContains("Matthew")
    }

    @Test
    fun clampsInvalidChapterInputToBookRange() {
        val api = FakeBibleApi(
            booksByTranslation = mapOf(
                "BSB" to listOf(Book("GEN", "Genesis", 3))
            ),
            chapterResponses = mapOf(
                ChapterKey("BSB", "GEN", 3) to listOf(Verse(1, "Third chapter verse", "GEN 3:1"))
            )
        )

        setAppContent(api)

        composeRule.onNodeWithTag("tab_bible").performClick()
        composeRule.onNodeWithTag("chapter_input").performTextClearance()
        composeRule.onNodeWithTag("chapter_input").performTextInput("999")
        composeRule.onNodeWithTag("load_button").performClick()
        composeRule.waitUntil(5_000) { api.lastRequest == ChapterKey("BSB", "GEN", 3) }
        composeRule.onNodeWithText("Third chapter verse").assertExists()
    }

    @Test
    fun showsEmptyStateWhenNoVersesReturned() {
        val api = FakeBibleApi(
            booksByTranslation = mapOf(
                "BSB" to listOf(Book("GEN", "Genesis", 50))
            ),
            chapterResponses = mapOf(
                ChapterKey("BSB", "GEN", 1) to emptyList()
            )
        )

        setAppContent(api)

        composeRule.onNodeWithTag("tab_bible").performClick()
        composeRule.onNodeWithTag("load_button").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("status_text").assertTextContains("No verses found")
    }

    @Test
    fun showsErrorStateWhenChapterLoadFails() {
        val api = FakeBibleApi(
            booksByTranslation = mapOf(
                "BSB" to listOf(Book("GEN", "Genesis", 50))
            ),
            chapterErrors = mapOf(
                ChapterKey("BSB", "GEN", 1) to IllegalStateException("boom")
            )
        )

        setAppContent(api)

        composeRule.onNodeWithTag("tab_bible").performClick()
        composeRule.onNodeWithTag("load_button").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("status_text").assertTextContains("Could not load")
    }

    private fun setAppContent(api: FakeBibleApi) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val localStore = LocalStore(context, "ui_test_${System.nanoTime()}").apply { clearAll() }
        val assetRepository = AssetRepository(context)

        composeRule.setContent {
            TheWordTheme {
                TheWordApp(
                    api = api,
                    localStore = localStore,
                    assetRepo = assetRepository
                )
            }
        }
        composeRule.waitForIdle()
    }

    private data class ChapterKey(val translationId: String, val bookId: String, val chapter: Int)

    private class FakeBibleApi(
        private val translations: List<Translation> = listOf(Translation("BSB", "Berean Standard Bible", "BSB")),
        private val booksByTranslation: Map<String, List<Book>> = mapOf(
            "BSB" to listOf(Book("GEN", "Genesis", 50))
        ),
        private val chapterResponses: Map<ChapterKey, List<Verse>> = mapOf(
            ChapterKey("BSB", "GEN", 1) to listOf(Verse(1, "In the beginning", "GEN 1:1"))
        ),
        private val chapterErrors: Map<ChapterKey, Throwable> = emptyMap()
    ) : BibleApi {
        var lastRequest: ChapterKey? = null

        override suspend fun fetchTranslations(): List<Translation> = translations

        override suspend fun fetchBooks(translationId: String): List<Book> = booksByTranslation[translationId].orEmpty()

        override suspend fun fetchChapter(translationId: String, bookId: String, chapter: Int): List<Verse> {
            val key = ChapterKey(translationId, bookId, chapter)
            lastRequest = key
            chapterErrors[key]?.let { throw it }
            return chapterResponses[key].orEmpty()
        }
    }
}
