package com.example.proyectohuertohogar.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaTiendasView(navController: NavController) {
    // Ubicación de ejemplo: Santiago de Chile (Cerca de La Moneda)
    val santiagoLocation = LatLng(-33.4429, -70.6539)

    // Configuramos la cámara inicial del mapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(santiagoLocation, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestras Tiendas") },
                navigationIcon = {
                    // Botón para volver atrás
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Componente del Mapa de Google
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                // Marcador 1: Tienda Central
                Marker(
                    state = MarkerState(position = santiagoLocation),
                    title = "HuertoHogar Centro",
                    snippet = "La mejor fruta fresca aquí."
                )

                // Marcador 2: Otra sucursal cercana
                Marker(
                    state = MarkerState(position = LatLng(-33.4450, -70.6550)),
                    title = "HuertoHogar Express",
                    snippet = "Solo retiro en tienda."
                )
            }
        }
    }
}