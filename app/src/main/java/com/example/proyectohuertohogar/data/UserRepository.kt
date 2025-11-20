package com.example.proyectohuertohogar.data

import android.util.Log
import com.example.proyectohuertohogar.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

/**
 * Repositorio: Capa intermedia que maneja la lógica de negocio y
 * decide si los datos provienen de la base de datos (local) o de la red (futuro).
 */
class UserRepository(private val userDao: UserDao) {

    // --- Funciones de Utilidad (Hash, Login, Registro, etc. - Las que ya creaste) ---
    private fun hashPassword(password: String): String {
        // ... (Tu código existente de hashPassword) ...
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
            hash.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error al hashear contraseña: ${e.message}")
            password
        }
    }

    suspend fun registerUser(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        // ... (Tu código existente de registerUser) ...
        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) { return@withContext false }
        val passwordHash = hashPassword(password)
        val newUser = User(email = email, passwordHash = passwordHash, name = "Usuario HuertoHogar")
        val result = userDao.insertUser(newUser)
        return@withContext result != -1L
    }

    suspend fun loginUser(email: String, password: String): User? = withContext(Dispatchers.IO) {
        // ... (Tu código existente de loginUser) ...
        val user = userDao.getUserByEmail(email)
        if (user == null) { return@withContext null }
        val enteredPasswordHash = hashPassword(password)
        return@withContext if (enteredPasswordHash == user.passwordHash) user else null
    }

    suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) { userDao.getUserByEmail(email) }

    suspend fun updateUser(user: User) = withContext(Dispatchers.IO) { userDao.updateUser(user) }

    // --- NUEVA FUNCIÓN: Catálogo de Productos Estático ---

    /**
     * Retorna la lista estática de productos para el catálogo.
     */
    fun getProducts(): List<Product> {
        return STATIC_PRODUCTS
    }
}

// Datos de productos estáticos (Mock Data) para el catálogo de HuertoHogar
private val STATIC_PRODUCTS = listOf(
    Product(
        id = "FR001",
        name = "Manzanas Fuji",
        price = 1990,
        unit = "kg",
        stock = 50,
        description = "Manzanas frescas y crujientes, ideales para comer o para repostería.",
        category = "Frutas Frescas",
        imageUrl = "https://placehold.co/100x100/50b86a/FFFFFF?text=Manzana"
    ),
    Product(
        id = "VR001",
        name = "Zanahorias Orgánicas",
        price = 1250,
        unit = "manojo",
        stock = 30,
        description = "Zanahorias cultivadas sin pesticidas, dulces y perfectas para jugos.",
        category = "Verduras Orgánicas",
        imageUrl = "https://placehold.co/100x100/f87171/FFFFFF?text=Zanahoria"
    ),
    Product(
        id = "VR002",
        name = "Espinacas Frescas",
        price = 990,
        unit = "paquete",
        stock = 45,
        description = "Paquete de espinacas frescas y listas para ensaladas o salteados.",
        category = "Verduras Orgánicas",
        imageUrl = "https://placehold.co/100x100/22c55e/FFFFFF?text=Espinaca"
    ),
    Product(
        id = "FR002",
        name = "Naranjas Valencia",
        price = 1500,
        unit = "kg",
        stock = 80,
        description = "Naranjas de jugo, dulces y con gran cantidad de vitamina C.",
        category = "Frutas Frescas",
        imageUrl = "https://placehold.co/100x100/f97316/FFFFFF?text=Naranja"
    ),
    Product(
        id = "PO001",
        name = "Miel de Abejas Orgánica",
        price = 3500,
        unit = "frasco (500g)",
        stock = 20,
        description = "Miel pura y orgánica, recolectada de forma sostenible.",
        category = "Productos Orgánicos",
        imageUrl = "https://placehold.co/100x100/f59e0b/FFFFFF?text=Miel"
    )
    // Se pueden añadir más productos aquí si es necesario
)