package com.example.proyectohuertohogar.data

import android.util.Log
import com.example.proyectohuertohogar.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) {

    // VARIABLE DE SESIÓN: Recuerda al usuario logueado
    var currentUser: User? = null

    private fun hashPassword(password: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
            hash.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) { password }
    }

    suspend fun registerUser(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) return@withContext false

        val passwordHash = hashPassword(password)
        val newUser = User(email = email, passwordHash = passwordHash)
        val id = userDao.insertUser(newUser)

        if (id != -1L) {
            // Iniciamos sesión automáticamente al registrar
            currentUser = newUser.copy(id = id.toInt())
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
            currentUser = user // Guardamos la sesión
            user
        } else {
            null
        }
    }

    // Función para actualizar los datos del perfil
    suspend fun updateUserProfile(name: String, phone: String, address: String, photoUri: String?) = withContext(Dispatchers.IO) {
        currentUser?.let { user ->
            val updatedUser = user.copy(
                name = name,
                phone = phone,
                address = address,
                photoUri = photoUri ?: user.photoUri
            )
            userDao.updateUser(updatedUser)
            currentUser = updatedUser // Actualizamos la sesión en memoria
        }
    }

    fun getProducts(): List<Product> {
        return STATIC_PRODUCTS
    }
}

// Datos de productos estáticos
// Datos de productos estáticos (Mock Data) para el catálogo de HuertoHogar
private val STATIC_PRODUCTS = listOf(
    Product(
        id = "FR001",
        name = "Manzanas Fuji",
        price = 1200,
        unit = "kg",
        stock = 150,
        description = "Manzanas crujientes y dulces, ideales para postres.",
        category = "Frutas Frescas",
        imageUrl = ""
    ),
    Product(
        id = "FR002",
        name = "Naranjas Valencia",
        price = 1000,
        unit = "kg",
        stock = 200,
        description = "Naranjas jugosas, perfectas para jugo natural.",
        category = "Frutas Frescas",
        imageUrl = ""
    ),
    Product(
        id = "FR003",
        name = "Plátanos Cavendish",
        price = 800,
        unit = "kg",
        stock = 80,
        description = "Plátanos ricos en potasio, ideales para deportistas.",
        category = "Frutas Frescas",
        imageUrl = ""
    ),
    Product(
        id = "VR001",
        name = "Zanahorias Orgánicas",
        price = 900,
        unit = "atado",
        stock = 100,
        description = "Cultivadas sin pesticidas, dulces y crujientes.",
        category = "Verduras Orgánicas",
        imageUrl = ""
    ),
    Product(
        id = "VR002",
        name = "Espinacas Frescas",
        price = 700,
        unit = "bolsa 500g",
        stock = 45,
        description = "Hojas verdes seleccionadas, listas para ensaladas.",
        category = "Verduras Orgánicas",
        imageUrl = ""
    ),
    Product(
        id = "VR003",
        name = "Pimientos Tricolores",
        price = 1500,
        unit = "pack 3u",
        stock = 60,
        description = "Mix de pimientos rojo, verde y amarillo.",
        category = "Verduras Orgánicas",
        imageUrl = ""
    ),
    Product(
        id = "PO001",
        name = "Miel de Abejas",
        price = 5000,
        unit = "frasco 500g",
        stock = 50,
        description = "Miel pura de ulmo, producción artesanal.",
        category = "Productos Orgánicos",
        imageUrl = ""
    ),
    Product(
        id = "PO003",
        name = "Quinua Real",
        price = 3200,
        unit = "bolsa 1kg",
        stock = 30,
        description = "Superalimento andino, lavado y listo para cocinar.",
        category = "Productos Orgánicos",
        imageUrl = ""
    ),
    Product(
        id = "PL001",
        name = "Leche Entera",
        price = 1100,
        unit = "litro",
        stock = 120,
        description = "Leche fresca de vaca, pasteurizada.",
        category = "Lácteos",
        imageUrl = ""
    ),
    Product(
        id = "PL002",
        name = "Queso de Cabra",
        price = 4500,
        unit = "pieza 250g",
        stock = 15,
        description = "Queso artesanal maduro con especias.",
        category = "Lácteos",
        imageUrl = ""
    )
)