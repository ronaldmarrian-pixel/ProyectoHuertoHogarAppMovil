package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectohuertohogar.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. Definimos el estado de la pantalla de Registro
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

    // --- Funciones de Manejo de Input ---

    fun updateEmail(newEmail: String) {
        _uiState.update { currentState -> currentState.copy(email = newEmail, emailError = null) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { currentState -> currentState.copy(password = newPassword, passwordError = null) }
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        _uiState.update { currentState -> currentState.copy(confirmPassword = newConfirmPassword, confirmPasswordError = null) }
    }

    // --- Lógica de Validación y Registro ---

    private fun validateInputs(): Boolean {
        var isValid = true
        val state = _uiState.value

        // 1. Validación de campos vacíos (similares a Login)
        if (state.email.isBlank()) { _uiState.update { it.copy(emailError = "El email no puede estar vacío.") }; isValid = false }
        if (state.password.isBlank()) { _uiState.update { it.copy(passwordError = "La contraseña no puede estar vacía.") }; isValid = false }
        if (state.confirmPassword.isBlank()) { _uiState.update { it.copy(confirmPasswordError = "Debes confirmar la contraseña.") }; isValid = false }

        // 2. Validación CRÍTICA: Contraseñas deben coincidir
        if (state.password.isNotEmpty() && state.confirmPassword.isNotEmpty() && state.password != state.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Las contraseñas no coinciden.") }
            isValid = false
        }

        return isValid
    }

    fun register() {
        if (!validateInputs()) return

        _uiState.update { it.copy(isLoading = true, registroError = null) }

        viewModelScope.launch {
            // Lógica de Registro: se implementará con Room/UserRepository
            // Por ahora, simulamos un éxito.

            // Reemplazar con: val success = userRepository.registerUser(email, password)
            val success = true

            if (success) {
                _uiState.update {
                    it.copy(isRegistroSuccessful = true, isLoading = false)
                }
            } else {
                _uiState.update {
                    it.copy(registroError = "Error al intentar registrar el usuario. (Email ya existe?)", isLoading = false)
                }
            }
        }
    }

    fun resetRegistroStatus() {
        _uiState.update { it.copy(isRegistroSuccessful = false) }
    }
}