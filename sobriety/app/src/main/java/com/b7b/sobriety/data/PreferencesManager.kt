package com.b7b.sobriety.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sobriety_prefs")

private const val DEFAULT_WEEKLY_SPEND_UGX = 50000

data class UserPreferences(
    val quitDate: String? = null,
    val weeklySpend: Int = DEFAULT_WEEKLY_SPEND_UGX,
    val personalReasons: List<String> = emptyList(),
    val longestStreak: Int = 0,
    val isDarkTheme: Boolean = false,
    val distractions: List<String> = listOf(
        "Go for a walk", "Drink water", "Call a friend", "Read 5 pages", "Do 10 pushups"
    ),
    val emergencyContacts: List<EmergencyContact> = emptyList()
)

data class EmergencyContact(val name: String, val info: String)

class PreferencesManager(private val context: Context) {

    companion object {
        private val KEY_QUIT_DATE = stringPreferencesKey("quit_date")
        private val KEY_WEEKLY_SPEND = intPreferencesKey("weekly_spend")
        private val KEY_REASONS = stringPreferencesKey("personal_reasons")
        private val KEY_LONGEST_STREAK = intPreferencesKey("longest_streak")
        private val KEY_DARK_THEME = booleanPreferencesKey("dark_theme")
        private val KEY_DISTRACTIONS = stringPreferencesKey("distractions")
        private val KEY_CONTACTS = stringPreferencesKey("emergency_contacts")
    }

    val preferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            quitDate = prefs[KEY_QUIT_DATE],
            weeklySpend = prefs[KEY_WEEKLY_SPEND] ?: DEFAULT_WEEKLY_SPEND_UGX,
            personalReasons = deserializeStringList(prefs[KEY_REASONS]),
            longestStreak = prefs[KEY_LONGEST_STREAK] ?: 0,
            isDarkTheme = prefs[KEY_DARK_THEME] ?: false,
            distractions = deserializeStringList(prefs[KEY_DISTRACTIONS]).ifEmpty {
                listOf("Go for a walk", "Drink water", "Call a friend", "Read 5 pages", "Do 10 pushups")
            },
            emergencyContacts = deserializeContacts(prefs[KEY_CONTACTS])
        )
    }

    suspend fun setQuitDate(date: String) {
        context.dataStore.edit { it[KEY_QUIT_DATE] = date }
    }

    suspend fun setWeeklySpend(spend: Int) {
        context.dataStore.edit { it[KEY_WEEKLY_SPEND] = spend }
    }

    suspend fun setPersonalReasons(reasons: List<String>) {
        context.dataStore.edit { it[KEY_REASONS] = serializeStringList(reasons) }
    }

    suspend fun setLongestStreak(streak: Int) {
        context.dataStore.edit { it[KEY_LONGEST_STREAK] = streak }
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { it[KEY_DARK_THEME] = isDark }
    }

    suspend fun setDistractions(list: List<String>) {
        context.dataStore.edit { it[KEY_DISTRACTIONS] = serializeStringList(list) }
    }

    suspend fun setEmergencyContacts(contacts: List<EmergencyContact>) {
        context.dataStore.edit { it[KEY_CONTACTS] = serializeContacts(contacts) }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }

    private fun serializeStringList(list: List<String>): String {
        return JSONArray(list).toString()
    }

    private fun deserializeStringList(json: String?): List<String> {
        if (json.isNullOrEmpty()) return emptyList()
        return try {
            val arr = JSONArray(json)
            (0 until arr.length()).map { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun serializeContacts(contacts: List<EmergencyContact>): String {
        val arr = JSONArray()
        contacts.forEach { c ->
            val obj = JSONObject()
            obj.put("name", c.name)
            obj.put("info", c.info)
            arr.put(obj)
        }
        return arr.toString()
    }

    private fun deserializeContacts(json: String?): List<EmergencyContact> {
        if (json.isNullOrEmpty()) return emptyList()
        return try {
            val arr = JSONArray(json)
            (0 until arr.length()).map { i ->
                val obj = arr.getJSONObject(i)
                EmergencyContact(obj.getString("name"), obj.getString("info"))
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
