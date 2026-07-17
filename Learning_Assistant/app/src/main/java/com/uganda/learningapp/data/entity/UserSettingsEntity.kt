package com.uganda.learningapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User settings (singleton - only one row)
 */
@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1, // Always 1 (singleton)
    val darkModeEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val studyReminderHour: Int = 9, // Default 9 AM
    val studyReminderMinute: Int = 0,
    val userName: String = "Learner"
)
