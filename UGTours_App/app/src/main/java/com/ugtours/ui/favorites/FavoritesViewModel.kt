package com.ugtours.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ugtours.data.repository.AttractionsRepository
import com.ugtours.models.Attraction
import com.ugtours.ui.common.UiState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for the Favorites screen.
 * Manages favorite attractions list with reactive updates.
 */
class FavoritesViewModel(
    private val attractionsRepository: AttractionsRepository
) : ViewModel() {
    
    /**
     * Reactive list of favorite attractions.
     * Automatically updates when favorites change.
     */
    val favorites: LiveData<UiState<List<Attraction>>> = 
        attractionsRepository.getFavoriteAttractions()
            .map { attractions ->
                if (attractions.isEmpty()) {
                    UiState.Empty("No favorites yet. Start exploring and add your favorite attractions!")
                } else {
                    UiState.Success(attractions)
                }
            }
            .asLiveData(viewModelScope.coroutineContext)
    
    /**
     * Reactive count of favorites.
     */
    val favoritesCount: LiveData<Int> = 
        attractionsRepository.getFavoritesCount()
            .asLiveData(viewModelScope.coroutineContext)
    
    /**
     * Remove an attraction from favorites.
     * @param attractionId The attraction ID to remove
     */
    fun removeFavorite(attractionId: Int) {
        viewModelScope.launch {
            attractionsRepository.removeFavorite(attractionId)
        }
    }
    
    /**
     * Clear all favorites.
     */
    fun clearAllFavorites() {
        viewModelScope.launch {
            attractionsRepository.clearAllFavorites()
        }
    }
}
