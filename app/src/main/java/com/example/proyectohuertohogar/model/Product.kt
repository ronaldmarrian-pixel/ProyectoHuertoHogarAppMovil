package com.example.proyectohuertohogar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad local (Domain Model) de Producto, usada por la base de datos Room.
 * Recibe la conversión de ProductApiModel.
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0, // Clave primaria local (la genera Room)
    val id: String, // ID externo (el que viene de la API, usado como referencia)
    val name: String,
    val price: Int,
    val unit: String, // Definido como "unidad" en tu toDomain()
    val stock: Int,
    val description: String,
    val category: String,
    val imageUrl: String
)