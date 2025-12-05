package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import com.example.proyectohuertohogar.data.CartRepository
import com.example.proyectohuertohogar.model.Product
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    val cartItems: StateFlow<List<Product>> = CartRepository.cartItems

    // Elimina UNA unidad del producto
    fun removeProduct(product: Product) {
        CartRepository.removeProduct(product)
    }

    // NUEVO: Vacía todo el carrito (para el botón "Vaciar Carrito")
    fun clearCart() {
        CartRepository.clearCart()
    }

    fun getTotal(): Int {
        return CartRepository.getTotal()
    }

    // Al hacer checkout, limpiamos el carrito
    fun checkout() {
        CartRepository.clearCart()
    }
}