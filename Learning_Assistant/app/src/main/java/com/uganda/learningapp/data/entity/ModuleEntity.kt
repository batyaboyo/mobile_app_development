package com.uganda.learningapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "modules")
data class ModuleEntity(
    @PrimaryKey val id: Int, // 1, 2, 3, 4
    val title: String, // "Phase 1: Foundations"
    val description: String,
    val weekRange: String // "Weeks 1-12"
)
