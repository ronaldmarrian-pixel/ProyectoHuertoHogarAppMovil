package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectohuertohogar.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. Agregamos 'isAdmin' al estado para que la UI sepa a dónde ir
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginError: String? = null,
    val isLoginSuccessful: Boolean = false,
    val isAdmin: Boolean = false // <--- NUEVO CAMPO
)

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, emailError = null, loginError = null) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, passwordError = null, loginError = null) }
    }

    fun login() {
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.update { it.copy(loginError = "Complete todos los campos") }
            return
        }

        _uiState.update { it.copy(isLoading = true, loginError = null) }

        viewModelScope.launch {
            // Llamamos al repositorio
            val user = userRepository.loginUser(_uiState.value.email, _uiState.value.password)

            if (user != null) {
                // 2. Si el usuario existe, guardamos si es admin o no en el estado
                _uiState.update {
                    it.copy(
                        isLoginSuccessful = true,
                        isLoading = false,
                        isAdmin = user.isAdmin // <--- AQUÍ CAPTURAMOS EL ROL
                    )
                }
            } else {
                _uiState.update { it.copy(loginError = "Credenciales inválidas.", isLoading = false) }
            }
        }
    }

    fun resetLoginStatus() {
        _uiState.update { it.copy(isLoginSuccessful = false, isAdmin = false) }
    }
}