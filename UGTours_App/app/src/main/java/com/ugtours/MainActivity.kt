package com.ugtours

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.ugtours.data.local.AppDatabase
import com.ugtours.data.repository.AttractionsRepository
import com.ugtours.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/**
 * Main Activity with navigation drawer and reactive favorites badge.
 * Uses AttractionsRepository for favorites count.
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var attractionsRepository: AttractionsRepository
    private lateinit var drawerToggle: ActionBarDrawerToggle
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize repository
        val database = AppDatabase.getDatabase(this)
        attractionsRepository = AttractionsRepository(
            database.favoritesDao(),
            database.recentlyViewedDao()
        )
        
        setSupportActionBar(binding.toolbar)
        
        // Setup navigation drawer
        setupDrawer()
        
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        binding.bottomNavigation.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(this)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_login, R.id.navigation_register -> {
                    binding.bottomNavigation.visibility = android.view.View.GONE
                    binding.toolbar.visibility = android.view.View.GONE
                    binding.drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    binding.bottomNavigation.visibility = android.view.View.VISIBLE
                    binding.toolbar.visibility = android.view.View.VISIBLE
                    binding.drawerLayout.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }
        
        // Observe favorites count reactively
        observeFavoritesBadge()
    }
    
    private fun setupDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        when (item.itemId) {
            R.id.navigation_profile -> {
                navController.navigate(R.id.navigation_profile)
            }
            R.id.navigation_about -> {
                navController.navigate(R.id.navigation_about)
            }
            R.id.navigation_settings -> {
                navController.navigate(R.id.navigation_settings)
            }
            R.id.action_logout -> {
                handleLogout()
                return true // Don't close drawer yet, will close after logout
            }
        }
        
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    
    private fun handleLogout() {
        // Clear user session
        lifecycleScope.launch {
            val preferencesRepo = com.ugtours.data.repository.UserPreferencesRepository(this@MainActivity)
            preferencesRepo.clearCurrentUserId()
            
            // Close drawer
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            
            // Navigate to login screen
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.navigation_login)
        }
    }
    
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    
    private fun observeFavoritesBadge() {
        lifecycleScope.launch {
            attractionsRepository.getFavoritesCount().collect { count ->
                val badge = binding.bottomNavigation.getOrCreateBadge(R.id.navigation_favorites)
                
                if (count > 0) {
                    badge.isVisible = true
                    badge.number = count
                    badge.backgroundColor = getColor(R.color.md_theme_light_tertiary)
                } else {
                    badge.isVisible = false
                }
            }
        }
    }
}
