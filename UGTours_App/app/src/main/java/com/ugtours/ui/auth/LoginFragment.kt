package com.ugtours.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ugtours.R
import com.ugtours.databinding.FragmentLoginBinding
import com.ugtours.ui.ViewModelFactory
import com.ugtours.ui.common.UiState

/**
 * Login Fragment with MVVM architecture.
 * Uses AuthViewModel for authentication logic.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        // Observe login state
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    setLoadingState(true)
                }
                is UiState.Success -> {
                    setLoadingState(false)
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }
                is UiState.Error -> {
                    setLoadingState(false)
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                is UiState.Empty -> {
                    setLoadingState(false)
                }
                UiState.Idle -> {
                    // Initial state, do nothing
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            
            // Call ViewModel to handle login
            viewModel.login(email, password)
        }

        binding.registerTextView.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_login_to_home)
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !isLoading
        binding.emailEditText.isEnabled = !isLoading
        binding.passwordEditText.isEnabled = !isLoading
        binding.registerTextView.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
