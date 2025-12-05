package com.example.proyectohuertohogar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectohuertohogar.model.Product
import com.example.proyectohuertohogar.model.User

// Usamos el nombre que ya tienes en tu proyecto: AppDatabase
@Database(entities = [User::class, Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Conectamos los DAOs
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, // Aquí referenciamos esta misma clase
                    "huerto_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}