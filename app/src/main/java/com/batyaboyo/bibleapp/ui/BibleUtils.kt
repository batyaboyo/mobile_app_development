package com.batyaboyo.bibleapp.ui

import com.batyaboyo.bibleapp.model.Book

fun getOldTestamentBooks(books: List<Book>): List<Book> {
    return books.filter { isOldTestament(it.id) }
}

fun getNewTestamentBooks(books: List<Book>): List<Book> {
    return books.filter { !isOldTestament(it.id) }
}

fun isOldTestament(bookId: String): Boolean {
    val otIds = listOf(
        "GEN", "EXO", "LEV", "NUM", "DEU", "JOS", "JDG", "RUT", "1SA", "2SA", "1KI", "2KI", "1CH", "2CH",
        "EZR", "NEH", "EST", "JOB", "PSA", "PRO", "ECC", "SNG", "ISA", "JER", "LAM", "EZK", "DAN", "HOS",
        "JOL", "AMO", "OBA", "JON", "MIC", "NAM", "HAB", "ZEP", "HAG", "ZEC", "MAL"
    )
    return bookId.uppercase() in otIds
}

fun selectDailyPrayer(prayers: List<com.batyaboyo.bibleapp.model.Prayer>, type: String, dayOfYear: Int): com.batyaboyo.bibleapp.model.Prayer? {
    val filtered = prayers.filter { it.type == type }
    if (filtered.isEmpty()) return null
    return filtered[dayOfYear % filtered.size]
}
