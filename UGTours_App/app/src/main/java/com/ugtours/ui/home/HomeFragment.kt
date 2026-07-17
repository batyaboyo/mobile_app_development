package com.ugtours.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ugtours.R
import com.ugtours.databinding.FragmentHomeBinding
import com.ugtours.ui.ViewModelFactory
import com.ugtours.ui.attractions.AttractionDetailActivity
import com.ugtours.ui.common.UiState

/**
 * Home Fragment with MVVM architecture.
 * Uses HomeViewModel for data management.
 */
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory(requireContext())
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupHeroSection()
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        // Observe featured attractions
        viewModel.featuredAttractions.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    setupFeaturedAttractions(state.data)
                }
                is UiState.Error -> {
                    // Handle error - could show error message
                }
                is UiState.Loading -> {
                    // Could show loading indicator
                }
                is UiState.Empty -> {
                    // Hide featured section
                }
                UiState.Idle -> {
                    // Initial state, do nothing
                }
            }
        }
        
        // Observe recently viewed
        viewModel.recentlyViewed.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    setupRecentlyViewed(state.data)
                }
                is UiState.Empty -> {
                    // Hide recently viewed section
                    binding.recentlyViewedSection.visibility = View.GONE
                    binding.recentlyViewedRecyclerView.visibility = View.GONE
                }
                else -> {
                    // Hide section for loading or error
                    binding.recentlyViewedSection.visibility = View.GONE
                    binding.recentlyViewedRecyclerView.visibility = View.GONE
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.exploreAllButton.setOnClickListener {
            navigateToAttractions()
        }
        
        binding.viewAllText.setOnClickListener {
            navigateToAttractions()
        }
    }
    
    private fun setupHeroSection() {
        // Load a random attraction image for the hero section
        val randomAttraction = viewModel.getRandomAttraction()
        
        randomAttraction?.let { attraction ->
            Glide.with(this)
                .load(attraction.imageUrls.first())
                .placeholder(R.drawable.placeholder_attraction)
                .error(R.drawable.placeholder_attraction)
                .centerCrop()
                .into(binding.heroImage)
        }
    }
    
    private fun setupRecentlyViewed(attractions: List<com.ugtours.models.Attraction>) {
        // Show section
        binding.recentlyViewedSection.visibility = View.VISIBLE
        binding.recentlyViewedRecyclerView.visibility = View.VISIBLE
        
        val adapter = FeaturedAttractionAdapter { attraction ->
            val intent = Intent(requireContext(), AttractionDetailActivity::class.java)
            intent.putExtra("ATTRACTION_ID", attraction.id)
            startActivity(intent)
        }
        
        binding.recentlyViewedRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
        
        adapter.submitList(attractions)
    }
    
    private fun setupFeaturedAttractions(attractions: List<com.ugtours.models.Attraction>) {
        val adapter = FeaturedAttractionAdapter { attraction ->
            val intent = Intent(requireContext(), AttractionDetailActivity::class.java)
            intent.putExtra("ATTRACTION_ID", attraction.id)
            startActivity(intent)
        }
        
        binding.featuredRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
        
        adapter.submitList(attractions)
    }
    
    private fun navigateToAttractions() {
        requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
            R.id.bottom_navigation
        )?.selectedItemId = R.id.navigation_attractions
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
