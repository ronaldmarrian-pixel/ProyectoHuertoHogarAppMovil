package com.example.proyectohuertohogar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectohuertohogar.data.UserRepository

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RegistroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return RegistroViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return CartViewModel() as T
        }
        // NUEVO: ProfileViewModel
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}