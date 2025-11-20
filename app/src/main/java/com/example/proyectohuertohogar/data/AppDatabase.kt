package com.example.proyectohuertohogar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Definimos las entidades (tablas) que tendrá la DB
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // El DAO que expone los comandos
    abstract fun userDao(): UserDao

    // 2. Singleton: Asegura que solo exista una instancia de la DB
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "huertohogar_database" // Nombre del archivo de la DB
                )
                    // SOLUCIÓN 1: Previene futuros errores si cambiamos la estructura de la tabla 'User'
                    .fallbackToDestructiveMigration()
                    // SOLUCIÓN 2: Permite que la DB se cree en el hilo principal
                    // (Esto EVITA el crash "keeps stopping" que estás viendo)
                    .allowMainThreadQueries()
                    .build() // Construye la base de datos
                INSTANCE = instance
                instance
            }
        }
    }
}