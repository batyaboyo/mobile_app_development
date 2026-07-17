package com.ugtours.data.local.dao

import androidx.room.*
import com.ugtours.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Favorite operations.
 * Provides methods to interact with the favorites table.
 */
@Dao
interface FavoritesDao {
    
    /**
     * Add an attraction to favorites.
     * @param favorite The favorite entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)
    
    /**
     * Remove an attraction from favorites by attraction ID.
     * @param attractionId The ID of the attraction to remove
     */
    @Query("DELETE FROM favorites WHERE attractionId = :attractionId")
    suspend fun removeFavorite(attractionId: Int)
    
    /**
     * Get all favorite attractions as a Flow.
     * Updates automatically when favorites change.
     * @return Flow of favorite entities ordered by most recently added
     */
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>
    
    /**
     * Get all favorite attraction IDs.
     * @return List of attraction IDs
     */
    @Query("SELECT attractionId FROM favorites ORDER BY addedAt DESC")
    suspend fun getFavoriteIds(): List<Int>
    
    /**
     * Check if an attraction is favorited.
     * @param attractionId The attraction ID to check
     * @return Flow of boolean indicating favorite status
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE attractionId = :attractionId)")
    fun isFavorite(attractionId: Int): Flow<Boolean>
    
    /**
     * Check if an attraction is favorited (non-reactive).
     * @param attractionId The attraction ID to check
     * @return true if favorited, false otherwise
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE attractionId = :attractionId)")
    suspend fun isFavoriteSync(attractionId: Int): Boolean
    
    /**
     * Get the count of favorite attractions.
     * @return Flow of the count
     */
    @Query("SELECT COUNT(*) FROM favorites")
    fun getFavoritesCount(): Flow<Int>
    
    /**
     * Get the count of favorite attractions (non-reactive).
     * @return The count
     */
    @Query("SELECT COUNT(*) FROM favorites")
    suspend fun getFavoritesCountSync(): Int
    
    /**
     * Clear all favorites.
     */
    @Query("DELETE FROM favorites")
    suspend fun clearAllFavorites()
}
