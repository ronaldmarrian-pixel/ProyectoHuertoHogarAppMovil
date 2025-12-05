package com.example.proyectohuertohogar.data

import android.util.Log
import com.example.proyectohuertohogar.data.remote.RetrofitClient
import com.example.proyectohuertohogar.model.Product
import com.example.proyectohuertohogar.model.User
import com.example.proyectohuertohogar.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.security.MessageDigest

// Constructor con 2 DAOs (Usuarios y Productos)
class UserRepository(
    private val userDao: UserDao,
    private val productDao: ProductDao
) {

    var currentUser: User? = null

    // ----------------------------------------------------------
    // 1. SEGURIDAD Y AUTH
    // ----------------------------------------------------------
    private fun hashPassword(password: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
            hash.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) { password }
    }

    // --- REGISTRO INTELIGENTE (PRIMER USUARIO = ADMIN) ---
    suspend fun registerUser(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        // 1. Verificar si el correo ya existe
        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) return@withContext false

        // 2. Verificar si es el PRIMER usuario en toda la app
        // (Nota: Necesitamos una forma de contar usuarios. Como no tenemos count(),
        // intentaremos ver si la tabla está vacía de otra forma o asumimos lógica de negocio).
        // Una forma simple es: si el ID que devuelve la DB es 1, es el primero.
        // Pero para estar seguros, haremos una lógica en el insert.

        // Vamos a determinar si debe ser admin ANTES de insertar
        // Truco: Intentamos buscar si existe AL MENOS UN usuario cualquiera.
        // Como tu DAO actual no tiene "getAllUsers", usaremos una lógica basada en el ID al insertar
        // o podemos asumir que el primer registro siempre tendrá ID 1 si la base está limpia.

        // MEJOR APROXIMACIÓN: Hacemos el insert normal.
        val passwordHash = hashPassword(password)

        // Por defecto no es admin
        var newUser = User(email = email, passwordHash = passwordHash, isAdmin = false)

        val id = userDao.insertUser(newUser)

        if (id != -1L) {
            // Si el ID es 1, significa que es el primer usuario en la historia de esta DB.
            // ¡Lo convertimos en Admin!
            if (id == 1L) {
                val adminUser = newUser.copy(id = id.toInt(), isAdmin = true)
                userDao.updateUser(adminUser) // Actualizamos para darle el poder
                currentUser = adminUser
            } else {
                currentUser = newUser.copy(id = id.toInt())
            }
            true
        } else {
            false
        }
    }

    suspend fun loginUser(email: String, password: String): User? = withContext(Dispatchers.IO) {
        val user = userDao.getUserByEmail(email)
        if (user == null) return@withContext null

        val enteredPasswordHash = hashPassword(password)
        if (enteredPasswordHash == user.passwordHash) {
            currentUser = user
            user
        } else {
            null
        }
    }

    suspend fun updateUserProfile(name: String, phone: String, address: String, photoUri: String?) = withContext(Dispatchers.IO) {
        currentUser?.let { user ->
            val updatedUser = user.copy(
                name = name,
                phone = phone,
                address = address,
                photoUri = photoUri ?: user.photoUri
            )
            userDao.updateUser(updatedUser)
            currentUser = updatedUser
        }
    }

    // ----------------------------------------------------------
    // 2. CREACIÓN DE ADMIN MANUAL (OBSOLETO / ELIMINADO)
    // ----------------------------------------------------------
    // Ya no necesitamos createDefaultAdmin() porque la lógica está en registerUser.
    // Dejamos la función vacía para que no te de error en HuertoHogarApp.kt si la llamas.
    suspend fun createDefaultAdmin() {
        // Lógica obsoleta. Ahora el primer usuario registrado será el admin.
    }

    // ----------------------------------------------------------
    // 3. CATÁLOGO PARA CLIENTES (Conexión API REST)
    // ----------------------------------------------------------
    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            val apiResponse = RetrofitClient.api.getProducts()
            val domainProducts = apiResponse.map { it.toDomain() }
            if (domainProducts.isNotEmpty()) return@withContext domainProducts
            return@withContext STATIC_PRODUCTS
        } catch (e: Exception) {
            Log.e("UserRepository", "Error API: ${e.message}")
            return@withContext STATIC_PRODUCTS
        }
    }

    // ----------------------------------------------------------
    // 4. GESTIÓN DE PRODUCTOS PARA ADMIN (Base de Datos Local)
    // ----------------------------------------------------------

    fun getAllProductsLocal(): Flow<List<Product>> = productDao.getAllProducts()

    suspend fun upsertProduct(product: Product) = withContext(Dispatchers.IO) {
        if (product.uid == 0) productDao.insertProduct(product) else productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.deleteProduct(product)
    }

    suspend fun initDefaultData() { }
}

// Datos de respaldo
private val STATIC_PRODUCTS = listOf(
    Product(id = "OFF1", name = "Manzanas (Offline)", price = 1000, unit = "kg", stock = 10, description = "Sin internet", category = "Frutas", imageUrl = ""),
    Product(id = "OFF2", name = "Naranjas (Offline)", price = 1000, unit = "kg", stock = 10, description = "Sin internet", category = "Frutas", imageUrl = "")
)

