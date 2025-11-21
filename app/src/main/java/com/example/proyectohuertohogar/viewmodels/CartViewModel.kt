package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import com.example.proyectohuertohogar.data.CartRepository
import com.example.proyectohuertohogar.model.Product
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    // Conectamos directamente con el repositorio singleton
    val cartItems: StateFlow<List<Product>> = CartRepository.cartItems

    fun removeProduct(product: Product) {
        CartRepository.removeProduct(product)
    }

    fun getTotal(): Int {
        return CartRepository.getTotal()
    }

    fun checkout() {
        // Aquí iría la lógica de guardar la compra en Room si fuera necesario
        // Por ahora, solo limpiamos el carrito simulando una compra exitosa
        CartRepository.clearCart()
    }
}