package com.example.proyectohuertohogar.navigation // Asegúrate que el package coincida

/**
 * Define las rutas (pantallas) de la aplicación.
 * Usamos una sealed class para tener rutas seguras.
 */
sealed class AppRoutes(val route: String) {

    // Pantallas de Autenticación
    data object Login : AppRoutes("login_screen")
    data object Registro : AppRoutes("registro_screen")

    // Pantallas Principales
    data object Home : AppRoutes("home_screen") // Catálogo
    data object Perfil : AppRoutes("perfil_screen")

    // Pantallas de Recursos Nativos
    data object MapaTiendas : AppRoutes("mapa_tiendas_screen")
    data object Camara : AppRoutes("camara_screen")
    data object Carrito : AppRoutes("carrito_screen")
}