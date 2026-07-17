package com.ugtours.data.local.dao

import androidx.room.*
import com.ugtours.data.local.entities.RecentlyViewedEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Recently Viewed operations.
 * Provides methods to interact with the recently_viewed table.
 */
@Dao
interface RecentlyViewedDao {
    
    /**
     * Add an attraction to recently viewed.
     * Updates the timestamp if already exists.
     * @param recentlyViewed The recently viewed entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecentlyViewed(recentlyViewed: RecentlyViewedEntity)
    
    /**
     * Get recently viewed attractions as a Flow.
     * Limited to the last 20 items, ordered by most recent.
     * @return Flow of recently viewed entities
     */
    @Query("SELECT * FROM recently_viewed ORDER BY viewedAt DESC LIMIT 20")
    fun getRecentlyViewed(): Flow<List<RecentlyViewedEntity>>
    
    /**
     * Get recently viewed attraction IDs.
     * Limited to the last 20 items.
     * @return List of attraction IDs
     */
    @Query("SELECT attractionId FROM recently_viewed ORDER BY viewedAt DESC LIMIT 20")
    suspend fun getRecentlyViewedIds(): List<Int>
    
    /**
     * Get the count of recently viewed attractions.
     * @return Flow of the count
     */
    @Query("SELECT COUNT(*) FROM recently_viewed")
    fun getRecentlyViewedCount(): Flow<Int>
    
    /**
     * Get the count of recently viewed attractions (non-reactive).
     * @return The count
     */
    @Query("SELECT COUNT(*) FROM recently_viewed")
    suspend fun getRecentlyViewedCountSync(): Int
    
    /**
     * Clear all recently viewed items.
     */
    @Query("DELETE FROM recently_viewed")
    suspend fun clearRecentlyViewed()
    
    /**
     * Remove old entries to keep only the last 20 items.
     * This is called automatically after adding new items.
     */
    @Query("""
        DELETE FROM recently_viewed 
        WHERE id NOT IN (
            SELECT id FROM recently_viewed 
            ORDER BY viewedAt DESC 
            LIMIT 20
        )
    """)
    suspend fun trimOldEntries()
}
