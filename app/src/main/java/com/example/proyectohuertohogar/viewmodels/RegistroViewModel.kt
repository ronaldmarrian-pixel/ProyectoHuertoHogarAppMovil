package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectohuertohogar.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistroUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val registroError: String? = null,
    val isRegistroSuccessful: Boolean = false
)

class RegistroViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun updateEmail(v: String) { _uiState.update { it.copy(email = v) } }
    fun updatePassword(v: String) { _uiState.update { it.copy(password = v) } }
    fun updateConfirmPassword(v: String) { _uiState.update { it.copy(confirmPassword = v) } }

    fun register() {
        val s = _uiState.value
        if (s.email.isBlank() || s.password.isBlank()) return
        if (s.password != s.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "No coinciden") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val success = userRepository.registerUser(s.email, s.password)
            if (success) {
                _uiState.update { it.copy(isRegistroSuccessful = true, isLoading = false) }
            } else {
                _uiState.update { it.copy(registroError = "El email ya existe", isLoading = false) }
            }
        }
    }

    fun resetRegistroStatus() {
        _uiState.update { it.copy(isRegistroSuccessful = false) }
    }
}