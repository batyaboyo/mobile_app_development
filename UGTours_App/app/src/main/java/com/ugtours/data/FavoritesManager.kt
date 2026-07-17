package com.ugtours.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages favorite attractions with persistent storage using SharedPreferences.
 * Favorites survive app restarts and are stored as a Set of attraction IDs.
 */
object FavoritesManager {
    private const val PREF_NAME = "UGToursFavorites"
    private const val KEY_FAVORITES = "favorite_ids"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Add an attraction to favorites
     */
    fun addFavorite(context: Context, attractionId: Int) {
        val prefs = getPrefs(context)
        val favorites = getAllFavoriteIds(context).toMutableSet()
        favorites.add(attractionId)
        prefs.edit().putStringSet(KEY_FAVORITES, favorites.map { it.toString() }.toSet()).apply()
    }

    /**
     * Remove an attraction from favorites
     */
    fun removeFavorite(context: Context, attractionId: Int) {
        val prefs = getPrefs(context)
        val favorites = getAllFavoriteIds(context).toMutableSet()
        favorites.remove(attractionId)
        prefs.edit().putStringSet(KEY_FAVORITES, favorites.map { it.toString() }.toSet()).apply()
    }

    /**
     * Toggle favorite status for an attraction
     * @return true if now favorited, false if unfavorited
     */
    fun toggleFavorite(context: Context, attractionId: Int): Boolean {
        return if (isFavorite(context, attractionId)) {
            removeFavorite(context, attractionId)
            false
        } else {
            addFavorite(context, attractionId)
            true
        }
    }

    /**
     * Check if an attraction is favorited
     */
    fun isFavorite(context: Context, attractionId: Int): Boolean {
        return getAllFavoriteIds(context).contains(attractionId)
    }

    /**
     * Get all favorite attraction IDs
     */
    fun getAllFavoriteIds(context: Context): Set<Int> {
        val prefs = getPrefs(context)
        val stringSet = prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
        return stringSet.mapNotNull { it.toIntOrNull() }.toSet()
    }

    /**
     * Clear all favorites
     */
    fun clearAllFavorites(context: Context) {
        getPrefs(context).edit().remove(KEY_FAVORITES).apply()
    }

    /**
     * Get count of favorites
     */
    fun getFavoritesCount(context: Context): Int {
        return getAllFavoriteIds(context).size
    }
}
