package com.ugtours.utils

import android.util.Patterns

/**
 * Sealed class representing validation results.
 */
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

/**
 * Utility object for input validation.
 */
object Validators {
    
    /**
     * Validates an email address.
     * @param email The email to validate
     * @return ValidationResult indicating success or error with message
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error("Email is required")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> 
                ValidationResult.Error("Please enter a valid email address")
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Validates a password.
     * Requirements: At least 8 characters, one digit, one uppercase letter
     * @param password The password to validate
     * @return ValidationResult indicating success or error with message
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> 
                ValidationResult.Error("Password is required")
            password.length < 8 -> 
                ValidationResult.Error("Password must be at least 8 characters")
            !password.any { it.isDigit() } -> 
                ValidationResult.Error("Password must contain at least one digit")
            !password.any { it.isUpperCase() } -> 
                ValidationResult.Error("Password must contain at least one uppercase letter")
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Validates that two passwords match.
     * @param password The password
     * @param confirmPassword The confirmation password
     * @return ValidationResult indicating success or error with message
     */
    fun validatePasswordMatch(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> 
                ValidationResult.Error("Please confirm your password")
            password != confirmPassword -> 
                ValidationResult.Error("Passwords do not match")
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Validates a name field.
     * @param name The name to validate
     * @return ValidationResult indicating success or error with message
     */
    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> 
                ValidationResult.Error("Name is required")
            name.length < 2 -> 
                ValidationResult.Error("Name must be at least 2 characters")
            !name.all { it.isLetter() || it.isWhitespace() } -> 
                ValidationResult.Error("Name can only contain letters")
            else -> ValidationResult.Success
        }
    }
}
