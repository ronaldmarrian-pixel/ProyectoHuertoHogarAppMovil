package com.example.proyectohuertohogar.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// 1. Definimos la entidad (tabla) "User"
@Entity(tableName = "users")
data class User(
    // 2. Definimos el ID autoincremental
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // 3. El email será el identificador de login
    val email: String,
    val passwordHash: String, // ¡Usaremos un hash para la contraseña, no texto plano!
    val name: String = "Usuario",
    val phone: String = "",
    val address: String = ""
)