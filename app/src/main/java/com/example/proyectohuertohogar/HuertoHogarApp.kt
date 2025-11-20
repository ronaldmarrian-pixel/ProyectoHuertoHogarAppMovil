package com.example.proyectohuertohogar

import android.app.Application
import com.example.proyectohuertohogar.data.AppDatabase
import com.example.proyectohuertohogar.data.UserRepository

class HuertoHogarApp : Application() {

    // Inicialización de la base de datos (DB)
    private val database by lazy { AppDatabase.getDatabase(this) }

    // Inicialización del repositorio, pasándole el DAO de la DB
    val userRepository by lazy { UserRepository(database.userDao()) }
}