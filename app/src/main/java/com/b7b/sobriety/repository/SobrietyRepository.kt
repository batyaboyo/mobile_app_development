package com.b7b.sobriety.repository

import com.b7b.sobriety.data.PreferencesManager
import com.b7b.sobriety.data.dao.CheckInDao
import com.b7b.sobriety.data.model.CheckIn
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

class SobrietyRepository(
    private val checkInDao: CheckInDao,
    private val preferencesManager: PreferencesManager
) {
    val preferencesFlow = preferencesManager.preferencesFlow
    val checkInsFlow = checkInDao.getAllFlow()

    suspend fun upsertCheckIn(checkIn: CheckIn) {
        checkInDao.upsert(checkIn)
    }

    suspend fun getCheckInByDate(date: String) = checkInDao.getByDate(date)

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    suspend fun calculateCurrentStreak(quitDateStr: String?, allCheckIns: List<CheckIn>): Int {
        if (quitDateStr == null) return 0
        
        val quitDate = LocalDate.parse(quitDateStr.split("T")[0], dateFormatter)
        val today = LocalDate.now()
        
        if (today.isBefore(quitDate)) return 0

        val checkIns = allCheckIns.associateBy { it.date }
        
        var currentDay = today
        var streak = 0
        
        // Loop backwards from today
        while (!currentDay.isBefore(quitDate)) {
            val dateStr = currentDay.format(dateFormatter)
            val checkIn = checkIns[dateStr]
            
            if (checkIn?.status == "slip") {
                break
            }
            
            // In the JS version, missing days are counted as sober if after quit date
            // status == "sober" OR null counts as sober, status == "slip" breaks
            streak++
            currentDay = currentDay.minusDays(1)
            
            if (streak > 3650) break // Safety
        }
        
        return streak
    }

    fun calculateMoneySaved(streakDays: Int, weeklySpend: Int): Int {
        val weeks = streakDays / 7.0
        return (weeks * weeklySpend).roundToInt()
    }

    fun getLongestResetDate(quitDateStr: String?, allCheckIns: List<CheckIn>): LocalDateTime {
        if (quitDateStr == null) return LocalDateTime.now()
        
        var lastReset = try {
            LocalDateTime.parse(quitDateStr, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: Exception) {
            LocalDate.parse(quitDateStr.split("T")[0], dateFormatter).atStartOfDay()
        }

        val slips = allCheckIns.filter { it.status == "slip" }.sortedBy { it.date }
        
        for (slip in slips) {
            val slipDate = LocalDate.parse(slip.date, dateFormatter).atTime(23, 59, 59, 999999999)
            if (slipDate.isAfter(lastReset)) {
                lastReset = slipDate.plusNanos(1)
            }
        }
        
        return lastReset
    }

    suspend fun setQuitDate(date: String) = preferencesManager.setQuitDate(date)
    suspend fun setWeeklySpend(spend: Int) = preferencesManager.setWeeklySpend(spend)
    suspend fun setPersonalReasons(reasons: List<String>) = preferencesManager.setPersonalReasons(reasons)
    suspend fun setLongestStreak(streak: Int) = preferencesManager.setLongestStreak(streak)
    suspend fun setDarkTheme(isDark: Boolean) = preferencesManager.setDarkTheme(isDark)
    suspend fun setDistractions(list: List<String>) = preferencesManager.setDistractions(list)
    suspend fun setEmergencyContacts(contacts: List<com.b7b.sobriety.data.EmergencyContact>) = 
        preferencesManager.setEmergencyContacts(contacts)

    suspend fun deleteAllData() {
        checkInDao.deleteAll()
        preferencesManager.clearAll()
    }
}
