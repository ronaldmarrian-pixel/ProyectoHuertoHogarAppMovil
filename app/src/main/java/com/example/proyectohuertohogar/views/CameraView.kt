package com.example.proyectohuertohogar.views

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.BitmapFactory
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraView(navController: NavController) {
    val context = LocalContext.current

    // Estado para guardar la URI de la imagen seleccionada (Foto o Galería)
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Estado temporal para la URI de la cámara (antes de tomar la foto)
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Bitmap para mostrar la imagen (ya que no usamos librerías externas como Coil)
    var bitmap by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }

    // 1. Lanzador para abrir la GALERÍA
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    // 2. Lanzador para abrir la CÁMARA
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = tempCameraUri
        }
    }

    // Efecto: Cuando cambia la URI, cargamos la imagen en el Bitmap para mostrarla
    LaunchedEffect(imageUri) {
        imageUri?.let { uri ->
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val androidBitmap = BitmapFactory.decodeStream(inputStream)
            bitmap = androidBitmap?.asImageBitmap()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- ÁREA DE LA FOTO DE PERFIL ---
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.size(160.dp)
            ) {
                // La imagen circular
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                } else {
                    // Icono por defecto si no hay foto
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(160.dp),
                        tint = Color.LightGray
                    )
                }

                // Botón pequeño de "Editar" sobre la foto
                SmallFloatingActionButton(
                    onClick = { /* Acción visual */ },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Opciones de Imagen", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTONES DE ACCIÓN ---

            // Botón CÁMARA
            Button(
                onClick = {
                    // Crear archivo temporal y lanzar cámara
                    val uri = createTempImageUri(context)
                    tempCameraUri = uri
                    cameraLauncher.launch(uri)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar Foto con Cámara")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón GALERÍA
            OutlinedButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar de Galería")
            }
        }
    }
}

// Función auxiliar para crear un archivo temporal donde la cámara guardará la foto
fun createTempImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(imageFileName, ".jpg", storageDir)

    // Asegúrate de que el "authority" coincida con lo que pusimos en AndroidManifest
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Usa el nombre del paquete dinámicamente
        image
    )
}