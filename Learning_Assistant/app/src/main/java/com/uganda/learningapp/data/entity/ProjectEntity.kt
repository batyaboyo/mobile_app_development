package com.uganda.learningapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a portfolio project that the user can track
 */
@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val phaseRef: String, // "Phase 1", "Phase 2", etc.
    val isCompleted: Boolean = false,
    val githubUrl: String = "", // User's GitHub repo link
    val notes: String = "", // User's personal notes
    val screenshotPath: String = "", // Local path to screenshot (optional)
    val weekId: Int = 0, // Associated week (0 = custom project)
    val createdDate: Long = System.currentTimeMillis(),
    val completedDate: Long? = null
)
