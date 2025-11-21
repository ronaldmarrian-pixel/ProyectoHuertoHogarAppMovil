package com.example.proyectohuertohogar.views

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectohuertohogar.HuertoHogarApp
import com.example.proyectohuertohogar.data.CartRepository
import com.example.proyectohuertohogar.model.Product
import com.example.proyectohuertohogar.navigation.AppRoutes
import com.example.proyectohuertohogar.viewmodels.HomeViewModel
import com.example.proyectohuertohogar.viewmodels.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    vm: HomeViewModel = viewModel(factory = ViewModelFactory(
        (LocalContext.current.applicationContext as HuertoHogarApp).userRepository))
) {
    val state by vm.uiState.collectAsState()
    val cartItems by CartRepository.cartItems.collectAsState()

    // Estado para controlar la animación de entrada
    var isVisible by remember { mutableStateOf(false) }

    // Efecto: Al cargar la vista, activamos la animación
    LaunchedEffect(Unit) {
        isVisible = true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo") },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.Carrito.route) }) {
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge { Text(cartItems.size.toString()) }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.Perfil.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                    IconButton(onClick = {
                        navController.navigate(AppRoutes.Login.route) {
                            popUpTo(AppRoutes.Home.route) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Salir")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(AppRoutes.MapaTiendas.route) },
                icon = { Icon(Icons.Default.Place, "Mapa") },
                text = { Text("Tiendas") }
            )
        }
    ) { paddingValues ->
        if (state.products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // ANIMACIÓN: Usamos AnimatedVisibility para que la lista entre suavemente
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(initialOffsetY = { 50 }),
                modifier = Modifier.padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "Productos Frescos",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(state.products) { product ->
                        ProductCard(product)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    val context = LocalContext.current

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ShoppingCart, null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$ ${product.price} / ${product.unit}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)

                Button(
                    onClick = {
                        CartRepository.addProduct(product)
                        Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(top = 4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Agregar", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}