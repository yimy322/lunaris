package dev.lunaris.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.lunaris.app.R
import dev.lunaris.app.ui.components.CustomButton
import dev.lunaris.app.ui.components.CustomTextField
import dev.lunaris.app.ui.navigation.Screen
import dev.lunaris.app.ui.screens.auth.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, vm: AuthViewModel = viewModel()){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading = vm.isLoading
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
                text = "¡Bienvenido de nuevo!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(10.dp))
            //imagen de bienvenida
            Image(
                painter = painterResource(id = R.drawable.login_illustration),
                contentDescription = "Onboarding Image",
                modifier = Modifier.fillMaxWidth(0.8f).aspectRatio(1f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField("Correo", value = email, onValueChange = { email = it })
            CustomTextField("Contraseña", value = password, onValueChange = { password = it }, isPassword = true)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Has olvidado tu Contraseña",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF09ABB5)
            )
            Spacer(modifier = Modifier.height(24.dp))
            //button de ingresar
            CustomButton(
                text = if (isLoading) "Ingresando..." else "Ingresar",
                enableButton = !isLoading,
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        vm.errorMessage = "Todos los campos son obligatorios"
                        return@CustomButton
                    }
                    vm.login(email, password) { success ->
                        if (success) {
                            navController.navigate(Screen.Board.route){
                                //elimina el historial de navegacion
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
                    text = "¿No tienes una cuenta? ",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Registrate",
                    color = Color(0xFF09ABB5),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
        }
    }
}