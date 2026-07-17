package com.ugtours.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity representing a recently viewed attraction.
 * Stores the attraction ID and when it was last viewed.
 */
@Entity(
    tableName = "recently_viewed",
    indices = [Index(value = ["attractionId"], unique = true)]
)
data class RecentlyViewedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val attractionId: Int,
    
    val viewedAt: Long = System.currentTimeMillis()
)
