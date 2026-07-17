package com.ugtours.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity representing a user in the database.
 * Stores user credentials with hashed password and salt.
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    
    val email: String,
    
    val phone: String = "",
    
    /**
     * Hashed password - NEVER store plain text passwords
     */
    val passwordHash: String,
    
    /**
     * Salt used for password hashing
     */
    val passwordSalt: String,
    
    val createdAt: Long = System.currentTimeMillis(),
    
    val updatedAt: Long = System.currentTimeMillis()
)
