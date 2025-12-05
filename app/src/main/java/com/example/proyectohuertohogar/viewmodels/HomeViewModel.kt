package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectohuertohogar.data.UserRepository
import com.example.proyectohuertohogar.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado actualizado con soporte para búsqueda y filtros
data class HomeUiState(
    val allProducts: List<Product> = emptyList(),       // Lista completa (copia de seguridad)
    val displayedProducts: List<Product> = emptyList(), // Lista filtrada (la que se ve)
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String = "Todos"
)

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val products = userRepository.getProducts()

            _uiState.update {
                it.copy(
                    allProducts = products,
                    displayedProducts = products, // Al inicio mostramos todo
                    isLoading = false
                )
            }
        }
    }

    // Función llamada cuando escribes en el buscador
    fun onSearchChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    // Función llamada cuando tocas una categoría
    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFilters()
    }

    // Lógica para filtrar la lista
    private fun applyFilters() {
        val currentState = _uiState.value
        val query = currentState.searchQuery.lowercase()
        val cat = currentState.selectedCategory

        val filtered = currentState.allProducts.filter { product ->
            val matchesSearch = product.name.lowercase().contains(query) ||
                    product.description.lowercase().contains(query)

            val matchesCategory = if (cat == "Todos") true else product.category == cat

            matchesSearch && matchesCategory
        }

        _uiState.update { it.copy(displayedProducts = filtered) }
    }
}