package com.example.proyectohuertohogar.model

/**
 * Define la estructura de un producto vendido en HuertoHogar.
 */
data class Product(
    val id: String, // ID único del producto (ej: FR001)
    val name: String,
    val price: Int,
    val unit: String, // Unidad de medida (ej: "kg", "unidad")
    val stock: Int,
    val description: String,
    val category: String,
    val imageUrl: String // URL para la imagen del producto
)