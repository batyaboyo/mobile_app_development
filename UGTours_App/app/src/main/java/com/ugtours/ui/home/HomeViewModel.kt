package com.ugtours.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ugtours.data.repository.AttractionsRepository
import com.ugtours.models.Attraction
import com.ugtours.ui.common.UiState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen.
 * Manages featured attractions, recently viewed, and categories.
 */
class HomeViewModel(
    private val attractionsRepository: AttractionsRepository
) : ViewModel() {
    
    // ========== Featured Attractions ==========
    
    private val _featuredAttractions = MutableLiveData<UiState<List<Attraction>>>()
    val featuredAttractions: LiveData<UiState<List<Attraction>>> = _featuredAttractions
    
    init {
        loadFeaturedAttractions()
    }
    
    /**
     * Load featured attractions (first 3 attractions).
     */
    fun loadFeaturedAttractions() {
        _featuredAttractions.value = UiState.Loading
        viewModelScope.launch {
            try {
                val attractions = attractionsRepository.getAllAttractions().take(3)
                _featuredAttractions.value = if (attractions.isEmpty()) {
                    UiState.Empty("No attractions available")
                } else {
                    UiState.Success(attractions)
                }
            } catch (e: Exception) {
                _featuredAttractions.value = UiState.Error(
                    e.message ?: "Failed to load attractions"
                )
            }
        }
    }
    
    // ========== Recently Viewed ==========
    
    /**
     * Recently viewed attractions as LiveData.
     * Automatically updates when data changes.
     */
    val recentlyViewed: LiveData<UiState<List<Attraction>>> = 
        attractionsRepository.getRecentlyViewedAttractions()
            .map { attractions ->
                if (attractions.isEmpty()) {
                    UiState.Empty("No recently viewed attractions")
                } else {
                    UiState.Success(attractions.take(5))
                }
            }
            .asLiveData(viewModelScope.coroutineContext)
    
    // ========== Categories ==========
    
    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories
    
    /**
     * Load all categories.
     */
    fun loadCategories() {
        viewModelScope.launch {
            val allCategories = attractionsRepository.getAllCategories()
            _categories.value = allCategories
        }
    }
    
    /**
     * Get a random attraction for the hero section.
     */
    fun getRandomAttraction(): Attraction? {
        return attractionsRepository.getAllAttractions().randomOrNull()
    }
}
