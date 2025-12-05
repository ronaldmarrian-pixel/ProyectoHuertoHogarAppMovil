package com.example.proyectohuertohogar.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home // <--- Nuevo ícono para el catálogo
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectohuertohogar.navigation.AppRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardView(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administrador") },
                actions = {
                    IconButton(onClick = {
                        // Cerrar sesión
                        navController.navigate(AppRoutes.Login.route) {
                            popUpTo(0)
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salir")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido, Admin",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- BOTÓN 1: GESTIONAR PRODUCTOS ---
            AdminMenuButton(
                text = "Gestionar Productos",
                icon = Icons.Filled.List,
                onClick = {
                    navController.navigate(AppRoutes.AdminInventario.route)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTÓN 2: VER CATÁLOGO (NUEVO) ---
            AdminMenuButton(
                text = "Ver Catálogo (Cliente)",
                icon = Icons.Filled.Home,
                onClick = {
                    // Navegamos al Home normal
                    navController.navigate(AppRoutes.Home.route)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTÓN 3: MI PERFIL ---
            AdminMenuButton(
                text = "Mi Perfil",
                icon = Icons.Filled.Person,
                onClick = {
                    navController.navigate(AppRoutes.Perfil.route)
                }
            )
        }
    }
}

// Componente reutilizable
@Composable
fun AdminMenuButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}