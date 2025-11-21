package com.example.proyectohuertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectohuertohogar.navigation.AppRoutes
import com.example.proyectohuertohogar.ui.theme.ProyectoHuertoHogarTheme
// Importamos TODAS las vistas, incluida la nueva del Carrito
import com.example.proyectohuertohogar.views.HomeView
import com.example.proyectohuertohogar.views.LoginView
import com.example.proyectohuertohogar.views.MapaTiendasView
import com.example.proyectohuertohogar.views.RegistroView
import com.example.proyectohuertohogar.views.CameraView
import com.example.proyectohuertohogar.views.CartView // <-- Importación nueva

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

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {
        // 1. LOGIN
        composable(AppRoutes.Login.route) {
            LoginView(navController = navController)
        }

        // 2. REGISTRO
        composable(AppRoutes.Registro.route) {
            RegistroView(navController = navController)
        }

        // 3. HOME (CATÁLOGO)
        composable(AppRoutes.Home.route) {
            HomeView(navController = navController)
        }

        // 4. PERFIL (Cámara y Galería - Recurso Nativo 2)
        composable(AppRoutes.Perfil.route) {
            CameraView(navController = navController)
        }

        // 5. MAPA DE TIENDAS (Recurso Nativo 1)
        composable(AppRoutes.MapaTiendas.route) {
            MapaTiendasView(navController = navController)
        }

        // 6. CARRITO DE COMPRAS (Funcionalidad Extra)
        composable(AppRoutes.Carrito.route) {
            CartView(navController = navController)
        }
    }
}