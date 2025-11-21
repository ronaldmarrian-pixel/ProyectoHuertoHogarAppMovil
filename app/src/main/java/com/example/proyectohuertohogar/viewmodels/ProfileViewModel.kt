package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectohuertohogar.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val photoUri: String? = null,
    val isSaved: Boolean = false,
    val isEditing: Boolean = false // NUEVO: Controla si estamos editando
)

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        userRepository.currentUser?.let { user ->
            _uiState.update {
                it.copy(
                    name = user.name,
                    phone = user.phone,
                    address = user.address,
                    photoUri = user.photoUri
                )
            }
        }
    }

    fun updateName(v: String) { _uiState.update { it.copy(name = v) } }
    fun updatePhone(v: String) { _uiState.update { it.copy(phone = v) } }
    fun updateAddress(v: String) { _uiState.update { it.copy(address = v) } }
    fun updatePhoto(uri: String) { _uiState.update { it.copy(photoUri = uri) } }

    // NUEVO: Funciones para cambiar modo
    fun startEditing() { _uiState.update { it.copy(isEditing = true) } }

    fun cancelEditing() {
        // Al cancelar, recargamos los datos originales y salimos del modo edición
        loadUserData()
        _uiState.update { it.copy(isEditing = false) }
    }

    fun saveProfile() {
        viewModelScope.launch {
            val s = _uiState.value
            userRepository.updateUserProfile(s.name, s.phone, s.address, s.photoUri)
            _uiState.update { it.copy(isSaved = true, isEditing = false) } // Salimos de edición al guardar
        }
    }

    fun resetSaveStatus() { _uiState.update { it.copy(isSaved = false) } }
}