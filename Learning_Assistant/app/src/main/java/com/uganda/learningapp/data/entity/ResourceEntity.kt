package com.uganda.learningapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a learning resource (course, video, GitHub repo, lab)
 */
@Entity(tableName = "resources")
data class ResourceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val type: String, // "Course", "YouTube", "GitHub", "Lab", "Documentation"
    val topic: String, // "Linux", "Networking", "Cybersecurity", "Python", "Blockchain", "Trading", "Git"
    val difficulty: String, // "Beginner", "Intermediate", "Advanced"
    val phaseId: Int, // 1-4
    val description: String = ""
)
