package com.uganda.learningapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "weeks",
    foreignKeys = [
        ForeignKey(entity = ModuleEntity::class, parentColumns = ["id"], childColumns = ["moduleId"])
    ],
    indices = [androidx.room.Index(value = ["moduleId"])]
)
data class WeekUnitEntity(
    @PrimaryKey val id: Int, 
    val moduleId: Int,
    val title: String, 
    val weekRangeLabel: String, 
    val description: String,
    val isCompleted: Boolean = false
)
