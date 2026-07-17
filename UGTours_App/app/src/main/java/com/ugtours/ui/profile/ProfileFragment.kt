package com.ugtours.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ugtours.R
import com.ugtours.databinding.FragmentProfileBinding
import com.ugtours.ui.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Profile Fragment with MVVM architecture.
 * Uses ProfileViewModel for user data and statistics.
 */
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory(requireContext())
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        loadUserData()
    }
    
    private fun setupObservers() {
        // Observe user statistics
        viewModel.userStats.observe(viewLifecycleOwner) { stats ->
            // Could display stats in UI if layout supports it
            // For now, this is available for future use
        }
        
        // Observe logout state
        viewModel.logoutState.observe(viewLifecycleOwner) { loggedOut ->
            if (loggedOut) {
                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                // Navigate to login screen
                findNavController().navigate(R.id.navigation_login)
            }
        }
    }
    
    private fun loadUserData() {
        // Load current user data
        lifecycleScope.launch {
            val userId = com.ugtours.data.repository.UserPreferencesRepository(requireContext())
                .currentUserIdFlow
                .first()
            
            if (userId != null) {
                // Get user from repository
                val authRepo = com.ugtours.data.repository.AuthRepository(
                    com.ugtours.data.local.AppDatabase.getDatabase(requireContext()).userDao()
                )
                val user = authRepo.getUserById(userId)
                
                if (user != null) {
                    binding.userNameText.text = user.name
                    binding.userEmailText.text = user.email
                } else {
                    binding.userNameText.text = "Guest User"
                    binding.userEmailText.text = "Not logged in"
                }
            } else {
                binding.userNameText.text = "Guest User"
                binding.userEmailText.text = "Not logged in"
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
