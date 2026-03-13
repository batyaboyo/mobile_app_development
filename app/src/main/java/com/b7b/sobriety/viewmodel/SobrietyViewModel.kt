package com.b7b.sobriety.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.b7b.sobriety.data.EmergencyContact
import com.b7b.sobriety.data.UserPreferences
import com.b7b.sobriety.data.model.CheckIn
import com.b7b.sobriety.repository.SobrietyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SobrietyUiState(
    val preferences: UserPreferences = UserPreferences(),
    val checkIns: List<CheckIn> = emptyList(),
    val currentStreak: Int = 0,
    val moneySaved: Int = 0,
    val liveTimer: String = "00d 00h 00m",
    val isLoading: Boolean = true
)

class SobrietyViewModel(
    private val repository: SobrietyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SobrietyUiState())
    val uiState: StateFlow<SobrietyUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.preferencesFlow,
                repository.checkInsFlow
            ) { prefs, checkIns ->
                val streak = repository.calculateCurrentStreak(prefs.quitDate)
                val money = repository.calculateMoneySaved(streak, prefs.weeklySpend)
                
                Triple(prefs, checkIns, Pair(streak, money))
            }.collect { (prefs, checkIns, stats) ->
                val (streak, money) = stats
                
                // Update longest streak if current is higher
                if (streak > prefs.longestStreak) {
                    repository.setLongestStreak(streak)
                }

                _uiState.value = SobrietyUiState(
                    preferences = prefs,
                    checkIns = checkIns,
                    currentStreak = streak,
                    moneySaved = money,
                    isLoading = false
                )
            }
        }

        // Live Timer Effect
        viewModelScope.launch {
            while (true) {
                val quitDate = _uiState.value.preferences.quitDate
                if (quitDate != null) {
                    val lastReset = repository.getLongestResetDate(quitDate)
                    val now = LocalDateTime.now()
                    val diff = java.time.Duration.between(lastReset, now)

                    if (diff.isNegative) {
                        _uiState.update { it.copy(liveTimer = "00d 00h 00m") }
                    } else {
                        val days = diff.toDays()
                        val hours = diff.toHours() % 24
                        val minutes = diff.toMinutes() % 60
                        val timerStr = "%02dd %02dh %02dm".format(days, hours, minutes)
                        _uiState.update { it.copy(liveTimer = timerStr) }
                    }
                }
                delay(30000) // Update every 30s as in JS
            }
        }
    }

    private suspend fun updateLongestStreak(streak: Int) {
        repository.setLongestStreak(streak)
    }

    // Actions
    fun completeOnboarding(quitDate: String, spend: Int, reasons: List<String>) {
        viewModelScope.launch {
            repository.setQuitDate(quitDate)
            repository.setWeeklySpend(spend)
            repository.setPersonalReasons(reasons)
        }
    }

    fun checkIn(checkIn: CheckIn) {
        viewModelScope.launch {
            repository.upsertCheckIn(checkIn)
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val current = _uiState.value.preferences.isDarkTheme
            repository.setDarkTheme(!current)
        }
    }

    fun addDistraction(text: String) {
        viewModelScope.launch {
            val current = _uiState.value.preferences.distractions
            repository.setDistractions(current + text)
        }
    }

    fun removeDistraction(index: Int) {
        viewModelScope.launch {
            val current = _uiState.value.preferences.distractions.toMutableList()
            if (index in current.indices) {
                current.removeAt(index)
                repository.setDistractions(current)
            }
        }
    }

    fun addContact(name: String, info: String) {
        viewModelScope.launch {
            val current = _uiState.value.preferences.emergencyContacts
            repository.setEmergencyContacts(current + EmergencyContact(name, info))
        }
    }

    fun removeContact(index: Int) {
        viewModelScope.launch {
            val current = _uiState.value.preferences.emergencyContacts.toMutableList()
            if (index in current.indices) {
                current.removeAt(index)
                repository.setEmergencyContacts(current)
            }
        }
    }

    fun resetData() {
        viewModelScope.launch {
            repository.deleteAllData()
        }
    }

    fun resetQuitDate() {
        viewModelScope.launch {
            val now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
            repository.setQuitDate(now)
        }
    }
}
