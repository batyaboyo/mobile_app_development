package com.ugtours.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ugtours.data.local.entities.UserEntity
import com.ugtours.data.repository.AuthRepository
import com.ugtours.data.repository.UserPreferencesRepository
import com.ugtours.ui.common.UiState
import com.ugtours.utils.ValidationResult
import com.ugtours.utils.Validators
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication (login and registration).
 * Handles form validation, authentication logic, and UI state.
 */
class AuthViewModel(
    private val authRepository: AuthRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    // ========== Login State ==========
    
    private val _loginState = MutableLiveData<UiState<UserEntity>>()
    val loginState: LiveData<UiState<UserEntity>> = _loginState
    
    /**
     * Attempt to login with email and password.
     * Validates input and calls repository.
     */
    fun login(email: String, password: String) {
        // Validate email
        when (val emailValidation = Validators.validateEmail(email)) {
            is ValidationResult.Error -> {
                _loginState.value = UiState.Error(emailValidation.message)
                return
            }
            ValidationResult.Success -> {}
        }
        
        // Check password not empty
        if (password.isBlank()) {
            _loginState.value = UiState.Error("Password is required")
            return
        }
        
        // Set loading state
        _loginState.value = UiState.Loading
        
        // Attempt login
        viewModelScope.launch {
            val result = authRepository.loginUser(email, password)
            
            result.onSuccess { user ->
                // Save user session
                preferencesRepository.saveCurrentUserId(user.id)
                _loginState.value = UiState.Success(user)
            }.onFailure { exception ->
                _loginState.value = UiState.Error(
                    exception.message ?: "Login failed"
                )
            }
        }
    }
    
    // ========== Registration State ==========
    
    private val _registrationState = MutableLiveData<UiState<Long>>()
    val registrationState: LiveData<UiState<Long>> = _registrationState
    
    /**
     * Attempt to register a new user.
     * Validates all input fields and calls repository.
     */
    fun register(name: String, email: String, password: String, confirmPassword: String) {
        // Validate name
        when (val nameValidation = Validators.validateName(name)) {
            is ValidationResult.Error -> {
                _registrationState.value = UiState.Error(nameValidation.message)
                return
            }
            ValidationResult.Success -> {}
        }
        
        // Validate email
        when (val emailValidation = Validators.validateEmail(email)) {
            is ValidationResult.Error -> {
                _registrationState.value = UiState.Error(emailValidation.message)
                return
            }
            ValidationResult.Success -> {}
        }
        
        // Validate password
        when (val passwordValidation = Validators.validatePassword(password)) {
            is ValidationResult.Error -> {
                _registrationState.value = UiState.Error(passwordValidation.message)
                return
            }
            ValidationResult.Success -> {}
        }
        
        // Validate password match
        when (val matchValidation = Validators.validatePasswordMatch(password, confirmPassword)) {
            is ValidationResult.Error -> {
                _registrationState.value = UiState.Error(matchValidation.message)
                return
            }
            ValidationResult.Success -> {}
        }
        
        // Set loading state
        _registrationState.value = UiState.Loading
        
        // Attempt registration
        viewModelScope.launch {
            val result = authRepository.registerUser(name, email, password)
            
            result.onSuccess { userId ->
                // Save user session
                preferencesRepository.saveCurrentUserId(userId)
                _registrationState.value = UiState.Success(userId)
            }.onFailure { exception ->
                _registrationState.value = UiState.Error(
                    exception.message ?: "Registration failed"
                )
            }
        }
    }
    
    /**
     * Reset login state (e.g., after showing error).
     */
    fun resetLoginState() {
        _loginState.value = UiState.Loading // Neutral state
    }
    
    /**
     * Reset registration state (e.g., after showing error).
     */
    fun resetRegistrationState() {
        _registrationState.value = UiState.Loading // Neutral state
    }
}
