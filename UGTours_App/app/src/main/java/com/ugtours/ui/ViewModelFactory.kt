package com.ugtours.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ugtours.data.local.AppDatabase
import com.ugtours.data.repository.AttractionsRepository
import com.ugtours.data.repository.AuthRepository
import com.ugtours.data.repository.BookingsRepository
import com.ugtours.data.repository.UserPreferencesRepository
import com.ugtours.ui.attractions.AttractionDetailViewModel
import com.ugtours.ui.attractions.AttractionsViewModel
import com.ugtours.ui.auth.AuthViewModel
import com.ugtours.ui.bookings.BookingsViewModel
import com.ugtours.ui.favorites.FavoritesViewModel
import com.ugtours.ui.home.HomeViewModel
import com.ugtours.ui.profile.ProfileViewModel

/**
 * Factory for creating ViewModels with dependencies.
 * Provides a centralized way to create ViewModels with required repositories.
 */
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    
    private val database = AppDatabase.getDatabase(context)
    
    private val authRepository = AuthRepository(database.userDao())
    private val attractionsRepository = AttractionsRepository(
        database.favoritesDao(),
        database.recentlyViewedDao()
    )
    private val bookingsRepository = BookingsRepository(database.bookingsDao())
    private val preferencesRepository = UserPreferencesRepository(context)
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository, preferencesRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(attractionsRepository) as T
            }
            modelClass.isAssignableFrom(AttractionsViewModel::class.java) -> {
                AttractionsViewModel(attractionsRepository) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(attractionsRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(authRepository, attractionsRepository, preferencesRepository) as T
            }
            modelClass.isAssignableFrom(BookingsViewModel::class.java) -> {
                BookingsViewModel(bookingsRepository, preferencesRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
    
    /**
     * Create AttractionDetailViewModel with attraction ID parameter.
     */
    fun createAttractionDetailViewModel(attractionId: Int): AttractionDetailViewModel {
        return AttractionDetailViewModel(attractionsRepository, attractionId)
    }
}
