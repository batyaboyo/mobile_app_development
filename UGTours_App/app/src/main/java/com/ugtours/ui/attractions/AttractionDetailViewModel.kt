package com.ugtours.ui.attractions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ugtours.data.repository.AttractionsRepository
import com.ugtours.models.Attraction
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for Attraction Detail screen.
 * Manages single attraction data, favorite status, and recently viewed tracking.
 */
class AttractionDetailViewModel(
    private val attractionsRepository: AttractionsRepository,
    private val attractionId: Int
) : ViewModel() {
    
    /**
     * Get the attraction by ID.
     */
    val attraction: Attraction? = attractionsRepository.getAttractionById(attractionId)
    
    /**
     * Reactive favorite status for this attraction.
     */
    val isFavorite: LiveData<Boolean> = attractionsRepository.isFavorite(attractionId)
        .asLiveData(viewModelScope.coroutineContext)
    
    init {
        // Track as recently viewed when detail screen is opened
        trackRecentlyViewed()
    }
    
    /**
     * Toggle favorite status for this attraction.
     * @return true if now favorited, false if removed
     */
    fun toggleFavorite(): LiveData<Boolean> {
        viewModelScope.launch {
            attractionsRepository.toggleFavorite(attractionId)
        }
        return isFavorite
    }
    
    /**
     * Track this attraction as recently viewed.
     */
    private fun trackRecentlyViewed() {
        viewModelScope.launch {
            attractionsRepository.addRecentlyViewed(attractionId)
        }
    }
}
