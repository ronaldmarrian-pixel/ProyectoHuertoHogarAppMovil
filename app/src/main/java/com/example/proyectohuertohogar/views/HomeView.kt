package com.example.proyectohuertohogar.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
    // 1. Inyección de dependencias simplificada
    vm: HomeViewModel = viewModel(factory = ViewModelFactory(
        (LocalContext.current.applicationContext as HuertoHogarApp).userRepository))
) {
    val state by vm.uiState.collectAsState()
    val cartItems by CartRepository.cartItems.collectAsState()

    val categories = listOf("Todos", "Frutas", "Verduras", "Lácteos", "Despensa")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo") },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.Carrito.route) }) {
                        BadgedBox(badge = { if (cartItems.isNotEmpty()) Badge { Text(cartItems.size.toString()) } }) {
                            Icon(Icons.Default.ShoppingCart, "Carrito")
                        }
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.Perfil.route) }) {
                        Icon(Icons.Default.Person, "Perfil")
                    }
                    IconButton(onClick = {
                        navController.navigate(AppRoutes.Login.route) { popUpTo(AppRoutes.Home.route) { inclusive = true } }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salir")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(AppRoutes.MapaTiendas.route) },
                icon = { Icon(Icons.Default.Place, null) },
                text = { Text("Tiendas") }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            // --- BARRA DE BÚSQUEDA ---
            OutlinedTextField(
                value = state.searchQuery, // Acceso al estado
                onValueChange = { vm.onSearchChanged(it) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar producto...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { vm.onSearchChanged("") }) { Icon(Icons.Default.Close, null) }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // --- FILTROS ---
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = state.selectedCategory == category,
                        onClick = { vm.onCategorySelected(category) },
                        label = { Text(category) },
                        leadingIcon = if (state.selectedCategory == category) {
                            { Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp)) }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null) {
                // Manejo de Error de la API
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error al cargar productos: ${state.error}", color = MaterialTheme.colorScheme.error)
                }
            }
            else {
                // --- LISTA DE PRODUCTOS ---
                AnimatedVisibility(
                    visible = state.displayedProducts.isNotEmpty(),
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(state.displayedProducts) { product -> // Acceso al estado
                            // Usamos el producto enriquecido
                            ProductCard(product, vm::onAddToCartClick)
                        }
                    }
                }
            }
        }
    }
}

// --- COMPONENTE MOCK: ProductCard.kt (para que compile) ---
// Normalmente este código estaría en ProductCard.kt, pero lo incluimos aquí para que tu proyecto corra.
@Composable
fun ProductCard(product: Product, onAddToCart: (Product) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text("$${product.price} | Stock: ${product.stock}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onAddToCart(product) }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar a carrito")
            }
        }
    }
}