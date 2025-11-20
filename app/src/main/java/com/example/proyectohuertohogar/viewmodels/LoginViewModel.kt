package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectohuertohogar.data.UserRepository // Todavía no existe, pero lo usaremos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. Definimos el estado de la pantalla de Login (datos y errores)
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginError: String? = null, // Error general (ej. credenciales inválidas)
    val isLoginSuccessful: Boolean = false
)

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    // El estado mutable que sólo el ViewModel puede modificar
    private val _uiState = MutableStateFlow(LoginUiState())
    // El estado inmutable que la vista (Composable) observa
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // --- Funciones de Manejo de Input ---

    fun updateEmail(newEmail: String) {
        _uiState.update { currentState ->
            currentState.copy(email = newEmail, emailError = null, loginError = null)
        }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(password = newPassword, passwordError = null, loginError = null)
        }
    }

    // --- Lógica de Validación y Login ---

    private fun validateInputs(): Boolean {
        // Validación básica: no dejar campos vacíos
        var isValid = true
        val state = _uiState.value

        if (state.email.isBlank()) {
            _uiState.update { it.copy(emailError = "El email no puede estar vacío.") }
            isValid = false
        }

        if (state.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "La contraseña no puede estar vacía.") }
            isValid = false
        }

        return isValid
    }

    fun login() {
        if (!validateInputs()) return

        _uiState.update { it.copy(isLoading = true, loginError = null) }

        viewModelScope.launch {
            // Lógica de Login: se implementará con Room/UserRepository en pasos posteriores
            // Por ahora, simulamos un éxito.

            // Reemplazar con: val success = userRepository.loginUser(email, password)
            val success = true

            if (success) {
                _uiState.update {
                    it.copy(isLoginSuccessful = true, isLoading = false)
                }
            } else {
                _uiState.update {
                    it.copy(loginError = "Credenciales inválidas.", isLoading = false)
                }
            }
        }
    }

    fun resetLoginStatus() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }
}