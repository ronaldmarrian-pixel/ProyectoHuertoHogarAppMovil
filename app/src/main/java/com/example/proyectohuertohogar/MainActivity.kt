package com.example.proyectohuertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectohuertohogar.navigation.AppRoutes
import com.example.proyectohuertohogar.ui.theme.ProyectoHuertoHogarTheme
import com.example.proyectohuertohogar.viewmodels.LoginViewModel

// Importaciones de tus Vistas
import com.example.proyectohuertohogar.views.HomeView
import com.example.proyectohuertohogar.views.LoginView
import com.example.proyectohuertohogar.views.MapaTiendasView
import com.example.proyectohuertohogar.views.RegistroView
import com.example.proyectohuertohogar.views.CameraView
import com.example.proyectohuertohogar.views.CartView
import com.example.proyectohuertohogar.views.CheckoutView
// IMPORTANTE: Importamos las dos vistas de Admin
import com.example.proyectohuertohogar.views.AdminProductView
import com.example.proyectohuertohogar.views.AdminDashboardView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoHuertoHogarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // 1. Obtenemos el contexto y la aplicación para acceder al Repositorio
    val context = LocalContext.current
    val app = context.applicationContext as HuertoHogarApp
    val userRepository = app.userRepository

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {
        // --- 1. LOGIN ---
        composable(AppRoutes.Login.route) {
            // Creamos el ViewModel aquí y se lo pasamos a la vista
            val loginViewModel = remember { LoginViewModel(userRepository) }

            LoginView(
                navController = navController,
                viewModel = loginViewModel
            )
        }

        // --- 2. REGISTRO ---
        composable(AppRoutes.Registro.route) {
            RegistroView(navController = navController)
        }

        // --- 3. HOME (CATÁLOGO CLIENTE) ---
        composable(AppRoutes.Home.route) {
            HomeView(navController = navController)
        }

        // --- 4. PERFIL (Cámara) ---
        composable(AppRoutes.Perfil.route) {
            CameraView(navController = navController)
        }

        // --- 5. MAPA DE TIENDAS ---
        composable(AppRoutes.MapaTiendas.route) {
            MapaTiendasView(navController = navController)
        }

        // --- 6. CARRITO ---
        composable(AppRoutes.Carrito.route) {
            CartView(navController = navController)
        }

        // --- 7. CHECKOUT ---
        composable(AppRoutes.Checkout.route) {
            CheckoutView(navController = navController)
        }

        // --- SECCIÓN ADMINISTRADOR (NUEVA ESTRUCTURA) ---

        // 8. DASHBOARD (Menú Principal)
        // Esta es la ruta a la que redirige el Login si eres Admin
        composable(AppRoutes.Admin.route) {
            AdminDashboardView(navController = navController)
        }

        // 9. INVENTARIO (Lista de Productos)
        // A esta ruta llegas pulsando el botón en el Dashboard
        composable(AppRoutes.AdminInventario.route) {
            AdminProductView(navController = navController)
        }
    }
}