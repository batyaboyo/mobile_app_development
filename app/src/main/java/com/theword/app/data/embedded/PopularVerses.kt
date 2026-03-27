package com.theword.app.data.embedded

import com.theword.app.domain.model.PopularVerse

object PopularVerses {
    val list = listOf(
        PopularVerse("John 3:16", "JHN", 3, verse = 16),
        PopularVerse("Philippians 4:13", "PHP", 4, verse = 13),
        PopularVerse("Jeremiah 29:11", "JER", 29, verse = 11),
        PopularVerse("Proverbs 3:5-6", "PRO", 3, startVerse = 5, endVerse = 6),
        PopularVerse("Romans 8:28", "ROM", 8, verse = 28),
        PopularVerse("Psalm 23:1", "PSA", 23, verse = 1),
        PopularVerse("Isaiah 41:10", "ISA", 41, verse = 10),
        PopularVerse("Matthew 6:33", "MAT", 6, verse = 33),
        PopularVerse("Joshua 1:9", "JOS", 1, verse = 9),
        PopularVerse("Psalm 46:1", "PSA", 46, verse = 1),
        PopularVerse("Romans 12:2", "ROM", 12, verse = 2),
        PopularVerse("Proverbs 16:3", "PRO", 16, verse = 3),
        PopularVerse("1 Corinthians 10:13", "1CO", 10, verse = 13),
        PopularVerse("Galatians 5:22-23", "GAL", 5, startVerse = 22, endVerse = 23),
        PopularVerse("Matthew 5:14-16", "MAT", 5, startVerse = 14, endVerse = 16),
    )
}
