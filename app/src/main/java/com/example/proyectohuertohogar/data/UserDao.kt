package com.example.proyectohuertohogar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    // Comando 1: Buscar un usuario por email para el Login
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    // Comando 2: Insertar un nuevo usuario para el Registro
    // onConflict = Ignore previene que se registren emails duplicados
    @Insert
    suspend fun insertUser(user: User): Long

    // Comando 3: Actualizar los datos del perfil
    @Update
    suspend fun updateUser(user: User)

    // Comando 4: Borrar todos los usuarios (Útil para tests/limpieza)
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}