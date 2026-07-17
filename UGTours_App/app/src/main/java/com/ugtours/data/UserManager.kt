package com.ugtours.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ugtours.models.User

object UserManager {
    private const val PREF_NAME = "UGToursPrefs"
    private const val KEY_USER = "user_session"
    private const val KEY_USERS_DB = "users_db" // Simulating a DB with SharedPreferences

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun registerUser(context: Context, user: User): Boolean {
        val prefs = getPrefs(context)
        val usersJson = prefs.getString(KEY_USERS_DB, "{}")
        val gson = Gson()
        
        // Use TypeToken for type-safe deserialization
        val type = object : TypeToken<MutableMap<String, Map<String, String>>>() {}.type
        val usersMap: MutableMap<String, Map<String, String>> = try {
            gson.fromJson(usersJson, type) ?: mutableMapOf()
        } catch (e: Exception) {
            mutableMapOf()
        }

        if (usersMap.containsKey(user.email)) {
            return false // User already exists
        }

        // Save user to "DB"
        // In a real app, we'd hash the password. Here we store as is for mock purposes.
        val userMap = mapOf(
            "email" to user.email,
            "password" to user.password,
            "name" to user.name
        )
        usersMap[user.email] = userMap
        
        prefs.edit().putString(KEY_USERS_DB, gson.toJson(usersMap)).apply()
        return true
    }

    fun loginUser(context: Context, email: String, password: String): Boolean {
        val prefs = getPrefs(context)
        val usersJson = prefs.getString(KEY_USERS_DB, "{}")
        val gson = Gson()
        
        // Use TypeToken for type-safe deserialization
        val type = object : TypeToken<Map<String, Map<String, String>>>() {}.type
        val usersMap: Map<String, Map<String, String>> = try {
            gson.fromJson(usersJson, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }

        val userMap = usersMap[email]
        if (userMap != null && userMap["password"] == password) {
            // Login successful, save session
            val user = User(
                id = 0L,
                name = userMap["name"] ?: "",
                email = email,
                phone = userMap["phone"] ?: "",
                password = password
            )
            saveUserSession(context, user)
            return true
        }
        return false
    }

    private fun saveUserSession(context: Context, user: User) {
        val prefs = getPrefs(context)
        val gson = Gson()
        prefs.edit().putString(KEY_USER, gson.toJson(user)).apply()
    }

    fun getCurrentUser(context: Context): User? {
        val prefs = getPrefs(context)
        val userJson = prefs.getString(KEY_USER, null) ?: return null
        return Gson().fromJson(userJson, User::class.java)
    }

    fun logout(context: Context) {
        getPrefs(context).edit().remove(KEY_USER).apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getCurrentUser(context) != null
    }
}
