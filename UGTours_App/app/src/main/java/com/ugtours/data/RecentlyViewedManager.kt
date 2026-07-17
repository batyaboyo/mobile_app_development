package com.ugtours.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Manages recently viewed attractions with persistent storage.
 * Tracks the last 10 attractions viewed by the user.
 */
object RecentlyViewedManager {
    private const val PREF_NAME = "UGToursRecentlyViewed"
    private const val KEY_RECENT_IDS = "recent_attraction_ids"
    private const val MAX_RECENT_ITEMS = 10

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Add an attraction to recently viewed list
     * Most recent items appear first
     */
    fun addRecentlyViewed(context: Context, attractionId: Int) {
        val prefs = getPrefs(context)
        val recentIds = getRecentlyViewedIds(context).toMutableList()
        
        // Remove if already exists (to move to top)
        recentIds.remove(attractionId)
        
        // Add to beginning of list
        recentIds.add(0, attractionId)
        
        // Keep only last MAX_RECENT_ITEMS
        val trimmedList = recentIds.take(MAX_RECENT_ITEMS)
        
        // Save to preferences
        val gson = Gson()
        prefs.edit().putString(KEY_RECENT_IDS, gson.toJson(trimmedList)).apply()
    }

    /**
     * Get list of recently viewed attraction IDs
     * Returns in order: most recent first
     */
    fun getRecentlyViewedIds(context: Context): List<Int> {
        val prefs = getPrefs(context)
        val json = prefs.getString(KEY_RECENT_IDS, "[]") ?: "[]"
        val gson = Gson()
        
        return try {
            val type = object : TypeToken<List<Int>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Clear all recently viewed items
     */
    fun clearRecentlyViewed(context: Context) {
        getPrefs(context).edit().remove(KEY_RECENT_IDS).apply()
    }

    /**
     * Get count of recently viewed items
     */
    fun getRecentlyViewedCount(context: Context): Int {
        return getRecentlyViewedIds(context).size
    }
}
