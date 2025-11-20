package dev.lunaris.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.lunaris.app.ui.components.CustomButton
import dev.lunaris.app.ui.components.CustomTextField
import dev.lunaris.app.ui.navigation.Screen
import dev.lunaris.app.ui.screens.auth.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, vm: AuthViewModel = viewModel()){
    //variables para el registro
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    val isLoading = vm.isLoading
    val errorMessage = vm.errorMessage

    //para la alerta notificacion
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        //para que muestre la alerta
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //titulo
            Text(
                text = "¡Bienvenido!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Empieza a organizar tus proyectos",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(42.dp))
            CustomTextField("Nombre completo", value = name, onValueChange = { name = it })
            CustomTextField("Correo", value = email, onValueChange = { email = it })
            CustomTextField("Contraseña", value = password, onValueChange = { password = it } , isPassword = true)
            CustomTextField("Confirmar contraseña", value = confirm, onValueChange = { confirm = it }, isPassword = true)

            Spacer(modifier = Modifier.height(42.dp))
            //button de registrarse
            CustomButton(
                text = if (isLoading) "Creando..." else "Registrarse",
                enableButton = !isLoading,
                onClick = {
                    //validacion de nulos
                    if (name.isBlank() || email.isBlank() || password.isBlank() || confirm.isBlank()) {
                        vm.errorMessage = "Todos los campos son obligatorios"
                        return@CustomButton
                    }
                    //para que las contrasenas coincidan
                    if (password != confirm) {
                        vm.errorMessage = "Las contraseñas no coinciden"
                        return@CustomButton
                    }
                    vm.register(name, email, password) { success ->
                        //retorna del register
                        if (success) {
                            //OK
                            navController.navigate(Screen.Login.route) {
                                //elimina el historial de navegacion
                                //cuando das back no te lleva a esta pantalla
                                popUpTo(0)
                            }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            //mensaje de error en la alerta
            vm.errorMessage?.let { message ->
                LaunchedEffect(message) {
                    snackbarHostState.showSnackbar(
                        message = message,
                        withDismissAction = true
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Inicia sesión",
                    color = Color(0xFF09ABB5),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
        }
    }
}