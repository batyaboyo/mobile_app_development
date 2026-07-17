package com.ugtours.ui.attractions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ugtours.databinding.FragmentAttractionsListBinding
import com.ugtours.ui.ViewModelFactory
import com.ugtours.ui.common.UiState

/**
 * Attractions List Fragment with MVVM architecture.
 * Uses AttractionsViewModel for search and filtering.
 */
class AttractionsListFragment : Fragment() {
    
    private var _binding: FragmentAttractionsListBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AttractionsViewModel by viewModels {
        ViewModelFactory(requireContext())
    }
    
    private lateinit var adapter: AttractionAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttractionsListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupSearch()
        setupCategoryFilters()
        setupObservers()
    }
    
    private fun setupRecyclerView() {
        adapter = AttractionAdapter { attraction ->
            val intent = Intent(requireContext(), AttractionDetailActivity::class.java)
            intent.putExtra("ATTRACTION_ID", attraction.id)
            startActivity(intent)
        }
        binding.attractionsRecyclerView.adapter = adapter
    }
    
    private fun setupSearch() {
        binding.searchBar.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val query = s?.toString() ?: ""
                viewModel.searchAttractions(query)
            }
        })
    }
    
    private fun setupCategoryFilters() {
        binding.categoryChips.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            
            // Clear search bar when filtering by category
            binding.searchBar.text?.clear()
            
            when (checkedIds[0]) {
                binding.chipAll.id -> {
                    viewModel.clearSearch()
                }
                binding.chipNationalPark.id -> {
                    // Filter attractions that contain "National Park" in category
                    viewModel.filterByCategory("National Park")
                }
                binding.chipWaterfall.id -> {
                    // Filter attractions that contain "Waterfall" in category
                    viewModel.filterByCategory("Waterfall")
                }
                binding.chipCultural.id -> {
                    // Filter attractions that contain "Cultural" in category
                    viewModel.filterByCategory("Cultural")
                }
            }
        }
    }
    
    private fun setupObservers() {
        // Observe attractions list with search/filter results
        viewModel.attractions.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    // Could show loading indicator
                    binding.emptyView.visibility = View.GONE
                }
                is UiState.Success -> {
                    adapter.submitList(state.data)
                    binding.emptyView.visibility = View.GONE
                    binding.attractionsRecyclerView.visibility = View.VISIBLE
                }
                is UiState.Empty -> {
                    adapter.submitList(emptyList())
                    binding.emptyView.visibility = View.VISIBLE
                    binding.attractionsRecyclerView.visibility = View.GONE
                }
                is UiState.Error -> {
                    // Handle error
                    binding.emptyView.visibility = View.VISIBLE
                    binding.attractionsRecyclerView.visibility = View.GONE
                }
                UiState.Idle -> {
                    // Initial state, do nothing
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
