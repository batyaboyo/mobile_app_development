package com.ugtours.data.local.dao

import androidx.room.*
import com.ugtours.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for User operations.
 * Provides methods to interact with the users table.
 */
@Dao
interface UserDao {
    
    /**
     * Insert a new user into the database.
     * @param user The user entity to insert
     * @return The row ID of the inserted user
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long
    
    /**
     * Get a user by email address.
     * @param email The email to search for
     * @return The user entity or null if not found
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
    
    /**
     * Get a user by ID.
     * @param userId The user ID to search for
     * @return The user entity or null if not found
     */
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserEntity?
    
    /**
     * Update an existing user.
     * @param user The user entity with updated values
     */
    @Update
    suspend fun updateUser(user: UserEntity)
    
    /**
     * Delete a user from the database.
     * @param user The user entity to delete
     */
    @Delete
    suspend fun deleteUser(user: UserEntity)
    
    /**
     * Check if a user with the given email exists.
     * @param email The email to check
     * @return true if user exists, false otherwise
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun userExists(email: String): Boolean
    
    /**
     * Get all users (for debugging/admin purposes).
     * @return Flow of all users
     */
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>
}
