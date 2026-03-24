package com.batyaboyo.bibleapp.ui

import com.batyaboyo.bibleapp.model.Prayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PrayerSelectionTest {

    @Test
    fun selectDailyPrayer_returnsNullWhenTypeMissing() {
        val prayers = listOf(
            Prayer("morning", "Morning", "v", "ref", "text", "amen")
        )

        val selected = selectDailyPrayer(prayers, "evening", 25)

        assertNull(selected)
    }

    @Test
    fun selectDailyPrayer_wrapsByDayIndex() {
        val prayers = listOf(
            Prayer("morning", "First", "v1", "ref1", "text1", "amen1"),
            Prayer("morning", "Second", "v2", "ref2", "text2", "amen2")
        )

        val selected = selectDailyPrayer(prayers, "morning", 3)

        assertEquals("Second", selected?.title)
    }
}
