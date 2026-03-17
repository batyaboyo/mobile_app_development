package com.b7b.sobriety.repository

import com.b7b.sobriety.data.PreferencesManager
import com.b7b.sobriety.data.dao.CheckInDao
import com.b7b.sobriety.data.model.CheckIn
import com.b7b.sobriety.util.DateUtils
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.floor

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

    suspend fun calculateCurrentStreak(quitDateStr: String?, allCheckIns: List<CheckIn>): Int {
        val quitDate = parseLocalDate(quitDateStr) ?: return 0
        val today = LocalDate.now()
        
        if (today.isBefore(quitDate)) return 0

        val checkIns = allCheckIns.associateBy { it.date }
        
        var currentDay = today
        var streak = 0
        
        while (!currentDay.isBefore(quitDate)) {
            val dateStr = currentDay.toString()
            if (checkIns[dateStr]?.status == "slip") break
            
            streak++
            currentDay = currentDay.minusDays(1)
            if (streak > 3650) break // Safety
        }
        return streak
    }

    private fun parseLocalDate(dateStr: String?): LocalDate? = try {
        DateUtils.parseDate(dateStr)
    } catch (e: Exception) {
        null
    }

    private fun parseLocalDateTime(dateTimeStr: String?): LocalDateTime? = try {
        DateUtils.parseDateTime(dateTimeStr)
    } catch (e: Exception) {
        null
    }

    fun calculateMoneySaved(quitDateStr: String?, allCheckIns: List<CheckIn>, weeklySpend: Int): Int {
        if (weeklySpend <= 0) return 0

        val lastReset = getLongestResetDate(quitDateStr, allCheckIns)
        val now = LocalDateTime.now()
        val soberMinutes = java.time.Duration.between(lastReset, now).toMinutes()

        if (soberMinutes <= 0) return 0

        val minutesPerWeek = 7.0 * 24 * 60
        val savedShillings = weeklySpend * (soberMinutes / minutesPerWeek)
        return floor(savedShillings).toInt()
    }

    fun getLongestResetDate(quitDateStr: String?, allCheckIns: List<CheckIn>): LocalDateTime {
        var lastReset = parseLocalDateTime(quitDateStr) ?: LocalDateTime.now()

        val slips = allCheckIns.filter { it.status == "slip" }.sortedBy { it.date }
        
        for (slip in slips) {
            val slipDate = parseLocalDate(slip.date)?.atTime(23, 59, 59, 999999999) ?: continue
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
