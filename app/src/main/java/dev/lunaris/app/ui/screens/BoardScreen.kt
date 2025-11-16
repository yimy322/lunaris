package dev.lunaris.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.lunaris.app.ui.components.CustomProjectCard
import dev.lunaris.app.ui.components.CustomTaskCard
import dev.lunaris.app.ui.theme.ColorPrimary
import dev.lunaris.app.R
import dev.lunaris.app.ui.navigation.Screen

@Composable
fun BoardScreen(navController: NavController){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_image), // tu imagen
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bienvenido, Yimy",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "fh2001@gmail.com",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Tus proyectos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Agregar proyecto",
                    tint = ColorPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            //proyectos
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                CustomProjectCard(
                    title = "Proyecto 1",
                    date = "NOV 02, 2025",
                    description = "Mi primer proyecto",
                    barColor = Color(0xFF8FC5FF),
                    onClick = {
                        navController.navigate(Screen.ProjectDetail.route)
                    }
                )
                CustomProjectCard(
                    title = "Proyecto 2",
                    date = "NOV 02, 2025",
                    description = "Mi segundo proyecto",
                    barColor = Color(0xFFE3B5FF),
                    onClick = {
                        navController.navigate(Screen.ProjectDetail.route)
                    }
                )
                CustomProjectCard(
                    title = "Proyecto 3",
                    date = "NOV 02, 2025",
                    description = "Mi tercer proyecto",
                    barColor = Color(0xFF74D788),
                    onClick = {
                        navController.navigate(Screen.ProjectDetail.route)
                    }
                )
                Button(
                    onClick = { navController.navigate(Screen.Project.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorPrimary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = "Ver mas proyectos",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            //tareas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tus tareas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ver más",
                    color = ColorPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(4) { index ->
                    CustomTaskCard(
                        title = "Tarea ${index + 1}",
                        description = "Esta es la tarea número ${index + 1}",
                        date = "25 Feb, 2025"
                    )
                }
            }
        }
    }
}