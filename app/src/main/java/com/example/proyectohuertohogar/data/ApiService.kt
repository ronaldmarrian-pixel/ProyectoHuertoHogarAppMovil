package com.example.proyectohuertohogar.data

import com.example.proyectohuertohogar.model.ProductApiModel
import retrofit2.http.GET

interface ApiService {

    // Define la función para obtener la lista de productos
    // Asume que la URL completa es: URL_BASE + "products"
    // El resultado esperado es una lista de objetos ProductApiModel
    @GET("products")
    suspend fun getProducts(): List<ProductApiModel>
}