package com.theword.app.domain.model

data class BibleBook(
    val id: String,
    val name: String,
    val chapters: Int,
    val order: Int
) {
    val isOldTestament: Boolean get() = order <= 39
}

data class JournalEntry(
    val id: Long,
    val title: String,
    val content: String,
    val timestamp: Long
)

data class Translation(
    val id: String,
    val name: String,
    val shortName: String,
    val language: String,
    val isDownloaded: Boolean = false
)

data class Commentary(
    val id: String,
    val name: String
)

data class Verse(
    val number: Int,
    val text: String,
    val parts: List<VersePart> = emptyList()
) {
    fun toMap() = mapOf(
        "number" to number,
        "text" to text,
        "parts" to parts.map { it.toMap() }
    )
}

sealed class VersePart {
    abstract fun toMap(): Map<String, Any>
    data class Text(val text: String) : VersePart() {
        override fun toMap() = mapOf("type" to "text", "text" to text)
    }
    data class Poem(val text: String, val indent: Int) : VersePart() {
        override fun toMap() = mapOf("type" to "poem", "text" to text, "indent" to indent)
    }
    data class Footnote(val caller: String, val text: String) : VersePart() {
        override fun toMap() = mapOf("type" to "footnote", "caller" to caller, "text" to text)
    }
}

sealed class ChapterContent {
    abstract fun toMap(): Map<String, Any>
    data class Heading(val text: String) : ChapterContent() {
        override fun toMap() = mapOf("type" to "heading", "text" to text)
    }
    data class VerseContent(val verse: Verse) : ChapterContent() {
        override fun toMap() = mapOf("type" to "verse", "verse" to verse.toMap())
    }
    data class LineBreak(val id: Int = 0) : ChapterContent() {
        override fun toMap() = mapOf("type" to "line_break")
    }
    data class HebrewSubtitle(val text: String) : ChapterContent() {
        override fun toMap() = mapOf("type" to "hebrew_subtitle", "text" to text)
    }
}

data class Bookmark(
    val reference: String,
    val text: String,
    val collection: String? = null,
    val bookmarkedAt: Long = System.currentTimeMillis()
)

data class Highlight(
    val reference: String,
    val color: String,
    val note: String? = null
)

data class QuizQuestion(
    val category: String,
    val question: String,
    val options: List<String>,
    val answerIndex: Int,
    val reference: String
)

data class QuizAnswer(
    val selectedIndex: Int,
    val correct: Boolean
)

data class BibleStory(
    val id: String,
    val title: String,
    val icon: String,
    val testament: String,
    val reference: String,
    val snippet: String,
    val sections: List<StorySection>,
    val moral: String,
    val keyVerse: KeyVerse
)

data class StorySection(
    val title: String,
    val text: String
)

data class KeyVerse(
    val text: String,
    val ref: String
)

data class Prayer(
    val time: String,
    val title: String,
    val verse: String,
    val verseRef: String,
    val text: String,
    val closing: String
)

data class PopularVerse(
    val ref: String,
    val bookId: String,
    val chapter: Int,
    val verse: Int? = null,
    val startVerse: Int? = null,
    val endVerse: Int? = null
)

data class CommentaryContent(
    val name: String,
    val bookName: String,
    val chapterNumber: Int,
    val introduction: String?,
    val sections: List<CommentarySection>
)

data class CommentarySection(
    val verseRange: String,
    val text: String
)
