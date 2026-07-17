package com.uganda.learningapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a gamification badge earned by the user
 */
@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey val id: String, // e.g., "week_1_complete", "phase_1_complete"
    val name: String,
    val description: String,
    val iconName: String, // Material icon reference
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null // Timestamp when unlocked
)
