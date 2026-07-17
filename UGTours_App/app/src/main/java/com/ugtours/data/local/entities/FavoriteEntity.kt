package com.ugtours.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity representing a favorite attraction.
 * Stores the attraction ID and when it was favorited.
 */
@Entity(
    tableName = "favorites",
    indices = [Index(value = ["attractionId"], unique = true)]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val attractionId: Int,
    
    val addedAt: Long = System.currentTimeMillis()
)
