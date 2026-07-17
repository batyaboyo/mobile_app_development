package com.ugtours.data.repository

import com.ugtours.data.AttractionsData
import com.ugtours.data.local.dao.FavoritesDao
import com.ugtours.data.local.dao.RecentlyViewedDao
import com.ugtours.data.local.entities.FavoriteEntity
import com.ugtours.data.local.entities.RecentlyViewedEntity
import com.ugtours.models.Attraction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Repository for attractions data.
 * Single source of truth for attractions, favorites, and recently viewed.
 */
class AttractionsRepository(
    private val favoritesDao: FavoritesDao,
    private val recentlyViewedDao: RecentlyViewedDao
) {
    
    /**
     * Get all attractions.
     * @return List of all attractions
     */
    fun getAllAttractions(): List<Attraction> {
        return AttractionsData.getAllAttractions()
    }
    
    /**
     * Get an attraction by ID.
     * @param id The attraction ID
     * @return The attraction or null if not found
     */
    fun getAttractionById(id: Int): Attraction? {
        return AttractionsData.getAttractionById(id)
    }
    
    /**
     * Search attractions by query.
     * @param query The search query
     * @return List of matching attractions
     */
    fun searchAttractions(query: String): List<Attraction> {
        return AttractionsData.searchAttractions(query)
    }
    
    /**
     * Get attractions by category.
     * @param category The category name
     * @return List of attractions in the category
     */
    fun getAttractionsByCategory(category: String): List<Attraction> {
        return AttractionsData.getAttractionsByCategory(category)
    }
    
    /**
     * Get all categories.
     * @return List of category names
     */
    fun getAllCategories(): List<String> {
        return AttractionsData.getAllCategories()
    }
    
    // ========== Favorites ==========
    
    /**
     * Add an attraction to favorites.
     * @param attractionId The attraction ID
     */
    suspend fun addFavorite(attractionId: Int) = withContext(Dispatchers.IO) {
        favoritesDao.addFavorite(FavoriteEntity(attractionId = attractionId))
    }
    
    /**
     * Remove an attraction from favorites.
     * @param attractionId The attraction ID
     */
    suspend fun removeFavorite(attractionId: Int) = withContext(Dispatchers.IO) {
        favoritesDao.removeFavorite(attractionId)
    }
    
    /**
     * Toggle favorite status for an attraction.
     * @param attractionId The attraction ID
     * @return true if now favorited, false if removed
     */
    suspend fun toggleFavorite(attractionId: Int): Boolean = withContext(Dispatchers.IO) {
        val isFavorite = favoritesDao.isFavoriteSync(attractionId)
        if (isFavorite) {
            favoritesDao.removeFavorite(attractionId)
            false
        } else {
            favoritesDao.addFavorite(FavoriteEntity(attractionId = attractionId))
            true
        }
    }
    
    /**
     * Check if an attraction is favorited (reactive).
     * @param attractionId The attraction ID
     * @return Flow of boolean
     */
    fun isFavorite(attractionId: Int): Flow<Boolean> {
        return favoritesDao.isFavorite(attractionId)
    }
    
    /**
     * Get all favorite attractions (reactive).
     * @return Flow of favorite attractions
     */
    fun getFavoriteAttractions(): Flow<List<Attraction>> {
        return favoritesDao.getAllFavorites().map { favorites ->
            favorites.mapNotNull { favorite ->
                AttractionsData.getAttractionById(favorite.attractionId)
            }
        }
    }
    
    /**
     * Get favorite attractions count (reactive).
     * @return Flow of count
     */
    fun getFavoritesCount(): Flow<Int> {
        return favoritesDao.getFavoritesCount()
    }
    
    /**
     * Clear all favorites.
     */
    suspend fun clearAllFavorites() = withContext(Dispatchers.IO) {
        favoritesDao.clearAllFavorites()
    }
    
    // ========== Recently Viewed ==========
    
    /**
     * Add an attraction to recently viewed.
     * @param attractionId The attraction ID
     */
    suspend fun addRecentlyViewed(attractionId: Int) = withContext(Dispatchers.IO) {
        recentlyViewedDao.addRecentlyViewed(
            RecentlyViewedEntity(attractionId = attractionId)
        )
        // Trim to keep only last 20
        recentlyViewedDao.trimOldEntries()
    }
    
    /**
     * Get recently viewed attractions (reactive).
     * @return Flow of recently viewed attractions
     */
    fun getRecentlyViewedAttractions(): Flow<List<Attraction>> {
        return recentlyViewedDao.getRecentlyViewed().map { recentlyViewed ->
            recentlyViewed.mapNotNull { recent ->
                AttractionsData.getAttractionById(recent.attractionId)
            }
        }
    }
    
    /**
     * Get recently viewed count (reactive).
     * @return Flow of count
     */
    fun getRecentlyViewedCount(): Flow<Int> {
        return recentlyViewedDao.getRecentlyViewedCount()
    }
    
    /**
     * Clear all recently viewed items.
     */
    suspend fun clearRecentlyViewed() = withContext(Dispatchers.IO) {
        recentlyViewedDao.clearRecentlyViewed()
    }
}
