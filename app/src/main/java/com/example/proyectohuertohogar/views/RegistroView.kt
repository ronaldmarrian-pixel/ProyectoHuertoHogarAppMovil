package com.example.proyectohuertohogar.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectohuertohogar.HuertoHogarApp
import com.example.proyectohuertohogar.navigation.AppRoutes
import com.example.proyectohuertohogar.viewmodels.RegistroViewModel
import com.example.proyectohuertohogar.viewmodels.ViewModelFactory

// 1. Importamos la nueva ruta de iconos "AutoMirrored"
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroView(
    navController: NavController,
    vm: RegistroViewModel = viewModel(factory = ViewModelFactory(
        (LocalContext.current.applicationContext as HuertoHogarApp).userRepository))
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(state.isRegistroSuccessful) {
        if (state.isRegistroSuccessful) {
            vm.resetRegistroStatus()
            navController.navigate(AppRoutes.Login.route) {
                popUpTo(AppRoutes.Registro.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("HuertoHogar - Registro") },
                navigationIcon = {
                    // 2. Usamos Icons.AutoMirrored.Filled.ArrowBack
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
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = vm::updateEmail,
                label = { Text("Email") },
                isError = state.emailError != null,
                supportingText = { if (state.emailError != null) Text(state.emailError!!) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = vm::updatePassword,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = state.passwordError != null,
                supportingText = { if (state.passwordError != null) Text(state.passwordError!!) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = vm::updateConfirmPassword,
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = state.confirmPasswordError != null,
                supportingText = { if (state.confirmPasswordError != null) Text(state.confirmPasswordError!!) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = vm::register,
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrar")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (state.registroError != null) {
                Text(state.registroError!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
} 