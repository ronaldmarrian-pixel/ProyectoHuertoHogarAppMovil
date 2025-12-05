package com.example.proyectohuertohogar.views

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectohuertohogar.HuertoHogarApp
import com.example.proyectohuertohogar.model.Product
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductView(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val app = context.applicationContext as HuertoHogarApp
    val userRepository = app.userRepository

    val productsState = userRepository.getAllProductsLocal().collectAsState(initial = emptyList())
    val products = productsState.value

    var showDialog by remember { mutableStateOf(false) }
    var productToEdit by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    productToEdit = null
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Nuevo")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(
                "Productos registrados: ${products.size}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(products) { product ->
                    AdminProductItem(
                        product = product,
                        onEdit = {
                            productToEdit = product
                            showDialog = true
                        },
                        onDelete = {
                            scope.launch { userRepository.deleteProduct(product) }
                        }
                    )
                }
            }
        }

        if (showDialog) {
            ProductFormDialog(
                product = productToEdit,
                onDismiss = { showDialog = false },
                onSave = { newProduct ->
                    scope.launch { userRepository.upsertProduct(newProduct) }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun ProductFormDialog(product: Product?, onDismiss: () -> Unit, onSave: (Product) -> Unit) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(product?.name ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }

    var photoUri by remember { mutableStateOf<Uri?>(if (!product?.imageUrl.isNullOrEmpty()) Uri.parse(product?.imageUrl) else null) }
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && tempUri != null) photoUri = tempUri
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(if (product == null) "Nuevo Producto" else "Editar Producto", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .clickable {
                            // --- CAMBIO CLAVE AQUÍ ---
                            // Usamos el nuevo nombre de la función para evitar conflictos
                            val uri = createProductImageUri(context)
                            tempUri = uri
                            launcher.launch(uri)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUri != null) {
                        AsyncImage(model = photoUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Icon(Icons.Default.Add, "Foto")
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, minLines = 3, modifier = Modifier.fillMaxWidth())

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }

                Spacer(Modifier.height(24.dp))

                Button(onClick = {
                    val newProd = Product(
                        uid = product?.uid ?: 0,
                        id = product?.id ?: System.currentTimeMillis().toString(),
                        name = name,
                        price = price.toIntOrNull() ?: 0,
                        stock = stock.toIntOrNull() ?: 0,
                        unit = "un",
                        description = description,
                        category = category,
                        imageUrl = photoUri?.toString() ?: ""
                    )
                    onSave(newProd)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Guardar")
                }
            }
        }
    }
}

@Composable
fun AdminProductItem(product: Product, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(2.dp)) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl, contentDescription = null,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text(product.description, maxLines = 1, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text("Stock: ${product.stock} | $${product.price}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Editar") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Borrar", tint = MaterialTheme.colorScheme.error) }
        }
    }
}

// --- FUNCIÓN RENOMBRADA PARA EVITAR CONFLICTOS ---
fun createProductImageUri(context: Context): Uri {
    val tempFile = File.createTempFile("huerto_prod_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile)
}