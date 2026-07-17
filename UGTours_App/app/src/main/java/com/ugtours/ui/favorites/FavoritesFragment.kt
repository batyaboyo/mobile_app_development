package com.ugtours.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ugtours.R
import com.ugtours.databinding.FragmentFavoritesBinding
import com.ugtours.ui.ViewModelFactory
import com.ugtours.ui.attractions.AttractionAdapter
import com.ugtours.ui.attractions.AttractionDetailActivity
import com.ugtours.ui.common.UiState

/**
 * Favorites Fragment with MVVM architecture.
 * Uses FavoritesViewModel for reactive favorites management.
 */
class FavoritesFragment : Fragment() {
    
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FavoritesViewModel by viewModels {
        ViewModelFactory(requireContext())
    }
    
    private lateinit var adapter: AttractionAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupRecyclerView() {
        adapter = AttractionAdapter { attraction ->
            val intent = Intent(requireContext(), AttractionDetailActivity::class.java)
            intent.putExtra("ATTRACTION_ID", attraction.id)
            startActivity(intent)
        }
        
        binding.favoritesRecyclerView.adapter = adapter
    }
    
    private fun setupObservers() {
        // Observe favorites list - automatically updates when favorites change
        viewModel.favorites.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data)
                    binding.emptyView.visibility = View.GONE
                    binding.favoritesRecyclerView.visibility = View.VISIBLE
                }
                is UiState.Empty -> {
                    adapter.submitList(emptyList())
                    binding.emptyView.visibility = View.VISIBLE
                    binding.favoritesRecyclerView.visibility = View.GONE
                }
                is UiState.Loading -> {
                    // Could show loading indicator
                }
                is UiState.Error -> {
                    // Handle error
                }
                UiState.Idle -> {
                    // Initial state, do nothing
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        // Handle explore button click in empty state
        binding.exploreButton.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottom_navigation
            )?.selectedItemId = R.id.navigation_attractions
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
