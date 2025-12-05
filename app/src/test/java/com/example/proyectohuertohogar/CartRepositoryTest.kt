package com.example.proyectohuertohogar

import com.example.proyectohuertohogar.data.CartRepository
import com.example.proyectohuertohogar.model.Product
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CartRepositoryTest {

    // Esta función se ejecuta antes de cada test para limpiar el carrito
    @Before
    fun setup() {
        CartRepository.clearCart()
    }

    @Test
    fun agregarProducto_deberiaAumentarTotal() {
        // 1. DADO (Given): Tenemos dos productos de prueba
        val manzana = Product(
            id = "1", name = "Manzana", price = 1200, unit = "kg",
            stock = 10, description = "", category = "", imageUrl = ""
        )
        val naranja = Product(
            id = "2", name = "Naranja", price = 1000, unit = "kg",
            stock = 10, description = "", category = "", imageUrl = ""
        )

        // 2. CUANDO (When): Los agregamos al carrito
        CartRepository.addProduct(manzana)
        CartRepository.addProduct(naranja)

        // 3. ENTONCES (Then): El total debería ser la suma de ambos (2200)
        val totalEsperado = 2200
        val totalReal = CartRepository.getTotal()

        // Verificamos si es verdad
        assertEquals("El total del carrito no es correcto", totalEsperado, totalReal)
    }

    @Test
    fun vaciarCarrito_deberiaDejarTotalEnCero() {
        // 1. Agregamos algo
        val pan = Product(
            id = "3", name = "Pan", price = 1500, unit = "kg",
            stock = 10, description = "", category = "", imageUrl = ""
        )
        CartRepository.addProduct(pan)

        // 2. Vaciamos
        CartRepository.clearCart()

        // 3. Verificamos que esté vacío
        assertEquals(0, CartRepository.getTotal())
    }
}