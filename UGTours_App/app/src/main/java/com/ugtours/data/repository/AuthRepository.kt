package com.ugtours.data.repository

import com.ugtours.data.local.dao.UserDao
import com.ugtours.data.local.entities.UserEntity
import com.ugtours.utils.PasswordHasher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for authentication operations.
 * Handles user registration, login, and password management.
 */
class AuthRepository(private val userDao: UserDao) {
    
    /**
     * Register a new user with hashed password.
     * @param name User's full name
     * @param email User's email address
     * @param password Plain text password (will be hashed)
     * @return Result with user ID on success, or error message
     */
    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Result<Long> = withContext(Dispatchers.IO) {
        try {
            // Check if user already exists
            if (userDao.userExists(email)) {
                return@withContext Result.failure(
                    Exception("An account with this email already exists")
                )
            }
            
            // Hash the password with salt
            val (hash, salt) = PasswordHasher.hashPasswordWithSalt(password)
            
            // Create user entity
            val user = UserEntity(
                name = name,
                email = email,
                passwordHash = hash,
                passwordSalt = salt
            )
            
            // Insert user and return ID
            val userId = userDao.insertUser(user)
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Login a user by verifying email and password.
     * @param email User's email address
     * @param password Plain text password to verify
     * @return Result with UserEntity on success, or error message
     */
    suspend fun loginUser(
        email: String,
        password: String
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            // Get user by email
            val user = userDao.getUserByEmail(email)
                ?: return@withContext Result.failure(
                    Exception("Invalid email or password")
                )
            
            // Verify password
            val isPasswordCorrect = PasswordHasher.verifyPassword(
                password,
                user.passwordHash,
                user.passwordSalt
            )
            
            if (!isPasswordCorrect) {
                return@withContext Result.failure(
                    Exception("Invalid email or password")
                )
            }
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get a user by ID.
     * @param userId The user ID
     * @return The user entity or null
     */
    suspend fun getUserById(userId: Long): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getUserById(userId)
    }
    
    /**
     * Update user information.
     * @param user The user entity with updated information
     */
    suspend fun updateUser(user: UserEntity) = withContext(Dispatchers.IO) {
        userDao.updateUser(user.copy(updatedAt = System.currentTimeMillis()))
    }
}
