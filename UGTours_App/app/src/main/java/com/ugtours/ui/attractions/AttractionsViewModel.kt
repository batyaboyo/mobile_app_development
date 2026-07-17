package com.ugtours.ui.attractions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ugtours.data.repository.AttractionsRepository
import com.ugtours.models.Attraction
import com.ugtours.ui.common.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for the Attractions List screen.
 * Handles search, filtering, and attraction list management.
 */
class AttractionsViewModel(
    private val attractionsRepository: AttractionsRepository
) : ViewModel() {
    
    private val _attractions = MutableLiveData<UiState<List<Attraction>>>()
    val attractions: LiveData<UiState<List<Attraction>>> = _attractions
    
    private var searchJob: Job? = null
    private var allAttractions: List<Attraction> = emptyList()
    
    init {
        loadAttractions()
    }
    
    /**
     * Load all attractions.
     */
    fun loadAttractions() {
        _attractions.value = UiState.Loading
        viewModelScope.launch {
            try {
                allAttractions = attractionsRepository.getAllAttractions()
                _attractions.value = if (allAttractions.isEmpty()) {
                    UiState.Empty("No attractions available")
                } else {
                    UiState.Success(allAttractions)
                }
            } catch (e: Exception) {
                _attractions.value = UiState.Error(
                    e.message ?: "Failed to load attractions"
                )
            }
        }
    }
    
    /**
     * Search attractions with debouncing.
     * @param query The search query
     */
    fun searchAttractions(query: String) {
        // Cancel previous search job
        searchJob?.cancel()
        
        if (query.isBlank()) {
            // Show all attractions if query is empty
            _attractions.value = UiState.Success(allAttractions)
            return
        }
        
        // Debounce search
        searchJob = viewModelScope.launch {
            delay(300) // Wait 300ms before searching
            
            try {
                val results = attractionsRepository.searchAttractions(query)
                _attractions.value = if (results.isEmpty()) {
                    UiState.Empty("No attractions found for \"$query\"")
                } else {
                    UiState.Success(results)
                }
            } catch (e: Exception) {
                _attractions.value = UiState.Error(
                    e.message ?: "Search failed"
                )
            }
        }
    }
    
    /**
     * Filter attractions by category.
     * @param category The category name
     */
    fun filterByCategory(category: String) {
        _attractions.value = UiState.Loading
        viewModelScope.launch {
            try {
                val results = attractionsRepository.getAttractionsByCategory(category)
                _attractions.value = if (results.isEmpty()) {
                    UiState.Empty("No attractions in category \"$category\"")
                } else {
                    UiState.Success(results)
                }
            } catch (e: Exception) {
                _attractions.value = UiState.Error(
                    e.message ?: "Filter failed"
                )
            }
        }
    }
    
    /**
     * Clear search and show all attractions.
     */
    fun clearSearch() {
        searchJob?.cancel()
        _attractions.value = UiState.Success(allAttractions)
    }
}
