package com.example.proyectohuertohogar.data

import com.example.proyectohuertohogar.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Un Singleton (Objeto único) para manejar los productos del carrito
 * en toda la aplicación.
 */
object CartRepository {
    // Lista mutable privada
    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())

    // Lista pública inmutable para que las vistas la observen
    val cartItems: StateFlow<List<Product>> = _cartItems.asStateFlow()

    // Función para agregar producto
    fun addProduct(product: Product) {
        _cartItems.update { currentList ->
            currentList + product
        }
    }

    // Función para quitar producto
    fun removeProduct(product: Product) {
        _cartItems.update { currentList ->
            currentList - product
        }
    }

    // Función para calcular el total
    fun getTotal(): Int {
        return _cartItems.value.sumOf { it.price }
    }

    // Limpiar carrito
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}