package com.ugtours.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ugtours.BuildConfig
import com.ugtours.data.FavoritesManager
import com.ugtours.data.RecentlyViewedManager
import com.ugtours.databinding.FragmentSettingsBinding
import com.ugtours.utils.ImageLoader

class SettingsFragment : Fragment() {
    
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        updateStats()
        setupClickListeners()
    }
    
    private fun updateStats() {
        // Update app version
        binding.appVersionText.text = BuildConfig.VERSION_NAME
        
        // Update favorites count
        val favoritesCount = FavoritesManager.getFavoritesCount(requireContext())
        binding.favoritesCountText.text = favoritesCount.toString()
        
        // Update recently viewed count
        val recentCount = RecentlyViewedManager.getRecentlyViewedCount(requireContext())
        binding.recentCountText.text = recentCount.toString()
    }
    
    private fun setupClickListeners() {
        // Clear image cache
        binding.clearCacheOption.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Clear Image Cache")
                .setMessage("This will clear all cached images. They will be re-downloaded when needed.")
                .setPositiveButton("Clear") { _, _ ->
                    ImageLoader.clearMemoryCache(requireContext())
                    ImageLoader.clearDiskCache(requireContext())
                    Toast.makeText(context, "Image cache cleared", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
        
        // Clear recently viewed
        binding.clearRecentOption.setOnClickListener {
            val recentCount = RecentlyViewedManager.getRecentlyViewedCount(requireContext())
            
            if (recentCount == 0) {
                Toast.makeText(context, "No recently viewed items", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            AlertDialog.Builder(requireContext())
                .setTitle("Clear Recently Viewed")
                .setMessage("This will remove all $recentCount recently viewed attractions from your history.")
                .setPositiveButton("Clear") { _, _ ->
                    RecentlyViewedManager.clearRecentlyViewed(requireContext())
                    updateStats()
                    Toast.makeText(context, "Recently viewed cleared", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
        
        // Clear favorites
        binding.clearFavoritesOption.setOnClickListener {
            val favoritesCount = FavoritesManager.getFavoritesCount(requireContext())
            
            if (favoritesCount == 0) {
                Toast.makeText(context, "No favorites to clear", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            AlertDialog.Builder(requireContext())
                .setTitle("Clear All Favorites")
                .setMessage("Are you sure you want to remove all $favoritesCount favorites? This cannot be undone.")
                .setPositiveButton("Clear All") { _, _ ->
                    FavoritesManager.clearAllFavorites(requireContext())
                    updateStats()
                    Toast.makeText(context, "All favorites cleared", Toast.LENGTH_SHORT).show()
                    
                    // Update MainActivity badge
                    requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                        com.ugtours.R.id.bottom_navigation
                    )?.getOrCreateBadge(com.ugtours.R.id.navigation_favorites)?.isVisible = false
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
    
    override fun onResume() {
        super.onResume()
        updateStats()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
