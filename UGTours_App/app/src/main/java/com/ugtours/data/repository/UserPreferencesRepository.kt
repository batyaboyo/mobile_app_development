package com.ugtours.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

// Extension property to create DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * Repository for user preferences and session management.
 * Uses DataStore for reactive preferences storage.
 */
class UserPreferencesRepository(private val context: Context) {
    
    companion object {
        private val CURRENT_USER_ID = longPreferencesKey("current_user_id")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PHONE = stringPreferencesKey("user_phone")
    }
    
    /**
     * Get the current logged-in user ID as a Flow.
     * @return Flow of user ID or null if not logged in
     */
    val currentUserIdFlow: Flow<Long?> = context.dataStore.data.map { preferences ->
        val userId = preferences[CURRENT_USER_ID]
        if (userId == -1L) null else userId
    }
    
    /**
     * Save the current user ID (login).
     * @param userId The user ID to save
     */
    suspend fun saveCurrentUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_USER_ID] = userId
        }
    }
    
    /**
     * Clear the current user ID (logout).
     */
    suspend fun clearCurrentUserId() {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_USER_ID] = -1L
        }
    }
    
    /**
     * Check if a user is logged in.
     * @return Flow of boolean
     */
    val isLoggedIn: Flow<Boolean> = currentUserIdFlow.map { it != null }
    
    /**
     * Save user details (email and phone).
     */
    suspend fun saveUserDetails(email: String, phone: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
            preferences[USER_PHONE] = phone
        }
    }
    
    /**
     * Get user ID synchronously (for ViewModelFactory).
     */
    fun getUserId(): Long {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[CURRENT_USER_ID] ?: -1L
            }.first()
        }
    }
    
    /**
     * Get user email synchronously.
     */
    fun getUserEmail(): String {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[USER_EMAIL] ?: ""
            }.first()
        }
    }
    
    /**
     * Get user phone synchronously.
     */
    fun getUserPhone(): String {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[USER_PHONE] ?: ""
            }.first()
        }
    }
}
