package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import com.example.proyectohuertohogar.data.UserRepository
import com.example.proyectohuertohogar.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Estado de la UI: contiene la lista de productos y si está cargando
data class HomeUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        // Simulamos carga y obtenemos los productos del repositorio
        _uiState.update { it.copy(isLoading = true) }
        val productList = userRepository.getProducts()
        _uiState.update { it.copy(products = productList, isLoading = false) }
    }
}