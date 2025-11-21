package com.example.proyectohuertohogar.views

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectohuertohogar.HuertoHogarApp
import com.example.proyectohuertohogar.viewmodels.ProfileUiState
import com.example.proyectohuertohogar.viewmodels.ProfileViewModel
import com.example.proyectohuertohogar.viewmodels.ViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.graphics.BitmapFactory
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraView(
    navController: NavController,
    vm: ProfileViewModel = viewModel(factory = ViewModelFactory(
        (LocalContext.current.applicationContext as HuertoHogarApp).userRepository))
) {
    val context = LocalContext.current
    val state by vm.uiState.collectAsState()

    // Mostrar mensaje de guardado
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            Toast.makeText(context, "Perfil Actualizado", Toast.LENGTH_SHORT).show()
            vm.resetSaveStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Editar Perfil" else "Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (state.isEditing) vm.cancelEditing() else navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (state.isEditing) {
                ProfileEditMode(state, vm, context)
            } else {
                ProfileReadMode(state, vm, context)
            }
        }
    }
}

// --- PANTALLA DE LECTURA (SOLO VER) ---
@Composable
fun ProfileReadMode(state: ProfileUiState, vm: ProfileViewModel, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Foto (Solo visualización)
        ProfileImage(state.photoUri, context, showEditIcon = false) {}

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = state.name.ifBlank { "Usuario Sin Nombre" },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tarjetas de información
        InfoCard(Icons.Default.Phone, "Teléfono", state.phone.ifBlank { "No registrado" })
        Spacer(modifier = Modifier.height(12.dp))
        InfoCard(Icons.Default.Place, "Dirección", state.address.ifBlank { "No registrada" })

        Spacer(modifier = Modifier.height(48.dp))

        // Botón para ir a Editar
        Button(
            onClick = { vm.startEditing() },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Icon(Icons.Default.Edit, null)
            Spacer(Modifier.width(8.dp))
            Text("Editar Perfil")
        }
    }
}

@Composable
fun InfoCard(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Text(value, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

// --- PANTALLA DE EDICIÓN (MODIFICAR DATOS) ---
@Composable
fun ProfileEditMode(state: ProfileUiState, vm: ProfileViewModel, context: Context) {
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { vm.updatePhoto(it.toString()) } }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success -> if (success) tempCameraUri?.let { vm.updatePhoto(it.toString()) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Foto (Clickeable para editar)
        Box(contentAlignment = Alignment.BottomEnd) {
            ProfileImage(state.photoUri, context, showEditIcon = true) {
                // Al hacer clic en la foto, no hacemos nada directo aquí, usamos los botones de abajo
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de Foto
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {
                // AQUÍ ES DONDE TE DABA ERROR: Llamamos a la función de abajo
                val uri = createTempImageUri(context)
                tempCameraUri = uri
                cameraLauncher.launch(uri)
            }) { Text("Cámara") }

            OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) { Text("Galería") }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(24.dp))

        // Formulario
        OutlinedTextField(
            value = state.name, onValueChange = vm::updateName,
            label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = state.phone, onValueChange = vm::updatePhone,
            label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = state.address, onValueChange = vm::updateAddress,
            label = { Text("Dirección de Despacho") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = { vm.cancelEditing() },
                modifier = Modifier.weight(1f).height(50.dp)
            ) { Text("Cancelar") }

            Button(
                onClick = { vm.saveProfile() },
                modifier = Modifier.weight(1f).height(50.dp)
            ) { Text("Guardar") }
        }
    }
}

// Componente auxiliar para mostrar la imagen circular
@Composable
fun ProfileImage(uriString: String?, context: Context, showEditIcon: Boolean, onClick: () -> Unit) {
    var bitmap by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }

    LaunchedEffect(uriString) {
        uriString?.let { str ->
            try {
                val inputStream = context.contentResolver.openInputStream(Uri.parse(str))
                bitmap = BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            } catch (e: Exception) { }
        }
    }

    Box(modifier = Modifier.size(140.dp)) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap!!, contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(140.dp).clip(CircleShape).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
        } else {
            Icon(Icons.Default.AccountCircle, null, modifier = Modifier.size(140.dp), tint = Color.LightGray)
        }

        if (showEditIcon) {
            SmallFloatingActionButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.BottomEnd),
                containerColor = MaterialTheme.colorScheme.primary
            ) { Icon(Icons.Default.Edit, null, tint = Color.White) }
        }
    }
}

// --- FUNCIÓN QUE FALTABA ---
// Esta función debe estar al final, FUERA de cualquier clase o composable
fun createTempImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(imageFileName, ".jpg", storageDir)

    // Asegúrate que "com.example.proyectohuertohogar" coincida con tu paquete real
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Esto usa tu nombre de paquete dinámicamente
        image
    )
}