package com.ugtours.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ugtours.databinding.FragmentRegisterBinding
import com.ugtours.ui.ViewModelFactory
import com.ugtours.ui.common.UiState

/**
 * Register Fragment with MVVM architecture.
 * Uses AuthViewModel for registration logic.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        // Observe registration state
        viewModel.registrationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    setLoadingState(true)
                }
                is UiState.Success -> {
                    setLoadingState(false)
                    Toast.makeText(
                        context,
                        "Registration successful! Welcome!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Navigate to home after successful registration
                    findNavController().navigate(com.ugtours.R.id.navigation_home)
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
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
            
            // Call ViewModel to handle registration
            viewModel.register(name, email, password, confirmPassword)
        }

        binding.loginTextView.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    
    private fun setLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.registerButton.isEnabled = !isLoading
        binding.nameEditText.isEnabled = !isLoading
        binding.emailEditText.isEnabled = !isLoading
        binding.passwordEditText.isEnabled = !isLoading
        binding.confirmPasswordEditText.isEnabled = !isLoading
        binding.loginTextView.isEnabled = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
