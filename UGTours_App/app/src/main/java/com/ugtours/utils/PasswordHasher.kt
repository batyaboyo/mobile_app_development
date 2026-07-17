package com.ugtours.utils

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/**
 * Utility object for secure password hashing and verification.
 * Uses SHA-256 with salt for password security.
 */
object PasswordHasher {
    
    private const val SALT_LENGTH = 16
    
    /**
     * Generates a random salt for password hashing.
     * @return Base64 encoded salt string
     */
    private fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }
    
    /**
     * Hashes a password with a salt using SHA-256.
     * @param password The plain text password
     * @param salt The salt to use for hashing
     * @return Base64 encoded hash
     */
    private fun hashPassword(password: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val saltBytes = Base64.getDecoder().decode(salt)
        md.update(saltBytes)
        val hash = md.digest(password.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }
    
    /**
     * Hashes a password and returns both the hash and salt.
     * @param password The plain text password
     * @return Pair of (hash, salt) both Base64 encoded
     */
    fun hashPasswordWithSalt(password: String): Pair<String, String> {
        val salt = generateSalt()
        val hash = hashPassword(password, salt)
        return Pair(hash, salt)
    }
    
    /**
     * Verifies a password against a stored hash and salt.
     * @param password The plain text password to verify
     * @param storedHash The stored password hash
     * @param storedSalt The stored salt
     * @return true if password matches, false otherwise
     */
    fun verifyPassword(password: String, storedHash: String, storedSalt: String): Boolean {
        val hash = hashPassword(password, storedSalt)
        return hash == storedHash
    }
}
