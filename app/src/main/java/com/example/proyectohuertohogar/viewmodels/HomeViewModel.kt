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

// 1. ESTADO DE LA UI: Define todos los campos que HomeView va a leer.
data class HomeUiState(
    val products: List<Product> = emptyList(),
    val displayedProducts: List<Product> = emptyList(), // Lista filtrada/buscada
    val searchQuery: String = "",
    val selectedCategory: String = "Todos",
    val isLoading: Boolean = false,
    val error: String? = null // <-- Este es el campo que HomeView necesita
)

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Llama al Repositorio, que llama a la API/Cache
                val productsList = userRepository.getProducts()

                _uiState.update {
                    it.copy(
                        products = productsList,
                        displayedProducts = productsList, // Inicialmente mostramos todos
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Error al cargar el catálogo: ${e.localizedMessage}",
                        isLoading = false
                    )
                }
            }
        }
    }

    // --- LÓGICA DE INTERACCIÓN (FALTANTE) ---

    // Función que la vista llama cuando se escribe en la barra de búsqueda
    fun onSearchChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterProducts(query, _uiState.value.selectedCategory)
    }

    // Función que la vista llama al seleccionar un Chip de Categoría
    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        filterProducts(_uiState.value.searchQuery, category)
    }

    // 2. FUNCIÓN DE CARRITO (Requerida por HomeView.kt)
    fun onAddToCartClick(product: Product) {
        // Lógica para añadir el producto al carrito
        // CartRepository.addProduct(product)
        // Por ahora lo dejamos vacío, pero su existencia es clave.
    }

    // --- FUNCIÓN PRIVADA DE FILTRADO ---
    private fun filterProducts(query: String, category: String) {
        val filteredList = _uiState.value.products
            .filter { product ->
                // Filtro por búsqueda
                product.name.contains(query, ignoreCase = true) || product.description.contains(query, ignoreCase = true)
            }
            .filter { product ->
                // Filtro por categoría
                if (category == "Todos") true else product.category == category
            }

        _uiState.update { it.copy(displayedProducts = filteredList) }
    }
}