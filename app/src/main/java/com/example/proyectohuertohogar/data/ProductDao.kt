package com.example.proyectohuertohogar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.proyectohuertohogar.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // 1. Obtener todos los productos para la cache local (Flow para tiempo real)
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    // 2. Insertar un producto (o reemplazar si ya existe)
    // Usamos OnConflictStrategy.REPLACE (CORREGIDO) para la sincronización con la API.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    // 3. Insertar una lista de productos (Usado para guardar la respuesta de la API)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<Product>)

    // 4. Actualizar un producto existente (Usado por el administrador)
    @Update
    suspend fun updateProduct(product: Product)

    // 5. Eliminar un producto (Usado por el administrador)
    @Delete
    suspend fun deleteProduct(product: Product)

    // 6. Obtener un producto por ID local (uid)
    @Query("SELECT * FROM products WHERE uid = :uid")
    suspend fun getProductByUid(uid: Int): Product?
}