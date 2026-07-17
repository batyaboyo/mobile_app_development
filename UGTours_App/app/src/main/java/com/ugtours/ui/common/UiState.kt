package com.ugtours.ui.common

/**
 * Sealed class representing different UI states.
 * Used to handle loading, success, error, and empty states in a type-safe manner.
 */
sealed class UiState<out T> {
    /**
     * Idle state - initial state before any operation
     */
    object Idle : UiState<Nothing>()
    
    /**
     * Loading state - operation in progress
     */
    object Loading : UiState<Nothing>()
    
    /**
     * Success state with data
     * @param data The successful result data
     */
    data class Success<T>(val data: T) : UiState<T>()
    
    /**
     * Error state with message
     * @param message Error message to display
     * @param exception Optional exception for debugging
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : UiState<Nothing>()
    
    /**
     * Empty state - no data available
     * @param message Optional message to display
     */
    data class Empty(val message: String = "No data available") : UiState<Nothing>()
}
