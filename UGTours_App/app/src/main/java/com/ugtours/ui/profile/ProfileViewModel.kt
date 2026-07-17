package com.ugtours.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ugtours.data.local.entities.UserEntity
import com.ugtours.data.repository.AttractionsRepository
import com.ugtours.data.repository.AuthRepository
import com.ugtours.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for the Profile screen.
 * Manages user data, statistics, and logout functionality.
 */
class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val attractionsRepository: AttractionsRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    /**
     * Current user data as LiveData.
     */
    val currentUser: LiveData<UserEntity?> = preferencesRepository.currentUserIdFlow
        .map { userId ->
            userId?.let { authRepository.getUserById(it) }
        }
        .asLiveData(viewModelScope.coroutineContext)
    
    /**
     * User statistics (favorites count, recently viewed count).
     */
    val userStats: LiveData<UserStats> = combine(
        attractionsRepository.getFavoritesCount(),
        attractionsRepository.getRecentlyViewedCount()
    ) { favoritesCount, recentlyViewedCount ->
        UserStats(
            favoritesCount = favoritesCount,
            recentlyViewedCount = recentlyViewedCount
        )
    }.asLiveData(viewModelScope.coroutineContext)
    
    private val _logoutState = MutableLiveData<Boolean>()
    val logoutState: LiveData<Boolean> = _logoutState
    
    /**
     * Get user details by current user ID.
     */
    fun loadUserDetails() {
        viewModelScope.launch {
            preferencesRepository.currentUserIdFlow.collect { userId ->
                userId?.let {
                    // User details are already exposed via currentUser LiveData
                }
            }
        }
    }
    
    /**
     * Logout the current user.
     */
    fun logout() {
        viewModelScope.launch {
            preferencesRepository.clearCurrentUserId()
            _logoutState.value = true
        }
    }
}

/**
 * Data class for user statistics.
 */
data class UserStats(
    val favoritesCount: Int = 0,
    val recentlyViewedCount: Int = 0
)
