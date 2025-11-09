package dev.lunaris.app.ui.screens

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.lunaris.app.R
import dev.lunaris.app.ui.navigation.Screen

@Composable
fun OnboardingScreen(navController: NavController){

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(40.dp))
            //imagen de bienvenida
            Image(
                painter = painterResource(id = R.drawable.onboarding_illustration),
                contentDescription = "Onboarding Image",
                modifier = Modifier.fillMaxWidth(0.8f).aspectRatio(1f)
            )
            //titulo
            Text(
                text = buildAnnotatedString {
                    append("Organiza tus ideas con ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF09ABB5),
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Lunaris")
                    }
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            //descripcion
            Text(
                text = "Gestiona tus tareas y proyectos fácilmente desde un solo lugar. Crea tableros, asigna actividades y mantén tu trabajo bajo control de forma clara y colaborativa.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            //button de comenzar
            Button(
                onClick = { navController.navigate(Screen.Login.route) },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(0),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF67B99A),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Comenzar", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

}