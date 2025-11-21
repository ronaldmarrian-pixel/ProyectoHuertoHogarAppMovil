package com.example.proyectohuertohogar.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val passwordHash: String,
    val name: String = "Usuario",
    val phone: String = "",
    val address: String = "",
    val photoUri: String? = null 
)