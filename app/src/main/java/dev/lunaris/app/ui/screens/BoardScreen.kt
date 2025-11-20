package dev.lunaris.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import dev.lunaris.app.ui.components.CustomProjectCard
import dev.lunaris.app.ui.components.CustomTaskCard
import dev.lunaris.app.ui.theme.ColorPrimary
import dev.lunaris.app.R
import dev.lunaris.app.data.repository.ProjectRepository
import dev.lunaris.app.ui.components.CreateProjectDialog
import dev.lunaris.app.ui.navigation.Screen
import dev.lunaris.app.ui.screens.auth.AuthViewModel
import dev.lunaris.app.ui.theme.DoneColor
import dev.lunaris.app.utils.toFormattedDate
import dev.lunaris.app.viewmodel.ProjectViewModel
import dev.lunaris.app.viewmodel.ProjectViewModelFactory

@Composable
fun BoardScreen(navController: NavController){
    //para salir de la sesion
    val authVm: AuthViewModel = viewModel()
    //crea la instancia del viewmodel
    val projectVm: ProjectViewModel = viewModel(
        factory = ProjectViewModelFactory(ProjectRepository())
    )
    //es como un init
    LaunchedEffect(Unit) {
        projectVm.loadProjects()
    }
    val projects = projectVm.projects
    //info del usuario logueado
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email
    val name = user?.displayName
    //para obtener solo el primer nombre
    val firstName = name?.trim()?.split(" ")?.firstOrNull() ?: "Usuario"
    //para el drop del usuario
    var expanded by remember { mutableStateOf(false) }
    //para el dialog de crear proyecto
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
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
                        painter = painterResource(id = R.drawable.profile_image),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable { expanded = true }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Mi perfil") },
                            onClick = {
                                expanded = false
                                //un dialog para mostrar el perfil
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Ajustes") },
                            onClick = {
                                expanded = false
                                //ir a ajustes
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Cerrar sesión", color = Color.Red) },
                            onClick = {
                                expanded = false
                                //cerramos sesion
                                authVm.signOut()
                                //redireccionamos al login
                                navController.navigate(Screen.Login.route) {
                                    //elimina del backstack
                                    //sino hacemos esto al dar back volvera a esta pantalla
                                    //aunque no este logueado
                                    popUpTo(Screen.Board.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bienvenido, $firstName",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = email ?: "",
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

                TextButton(
                    onClick = { showDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "crear proyecto",
                        tint = ColorPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Crear",
                        color = ColorPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            //proyectos
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (projects.isEmpty()) {
                    Text(
                        text = "Aún no tienes proyectos",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                } else {
                    //para solo mostrar 3
                    projects.take(3).forEach { project ->
                        CustomProjectCard(
                            title = project.title,
                            date = project.createdAt.toFormattedDate(),
                            description = project.description,
                            barColor = Color(project.colorHex.toColorInt()),
                            onClick = {
                                navController.navigate(Screen.ProjectDetail.createRoute(project.id))
                            }
                        )
                    }
                }
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
        }
        if (showDialog) {
            CreateProjectDialog(
                onDismiss = { showDialog = false },
                onCreate = { title, description, color ->
                    //guardamos
                    projectVm.createProject(title, description, color)
                    showDialog = false
                }
            )
        }
    }
}