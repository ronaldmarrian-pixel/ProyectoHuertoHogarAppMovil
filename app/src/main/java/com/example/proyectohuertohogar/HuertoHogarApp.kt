package com.example.proyectohuertohogar

import android.app.Application
import com.example.proyectohuertohogar.data.AppDatabase // Importamos la clase correcta
import com.example.proyectohuertohogar.data.UserRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HuertoHogarApp : Application() {

    // 1. Instancia de la base de datos usando AppDatabase
    private val database by lazy {
        AppDatabase.getDatabase(this)
    }

    // 2. Instancia del repositorio (inyectamos los DAOs)
    val userRepository by lazy {
        UserRepository(
            userDao = database.userDao(),
            productDao = database.productDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        // Inicialización opcional
        MainScope().launch {
            userRepository.initDefaultData()
        }
    }
}