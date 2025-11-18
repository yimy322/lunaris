package dev.lunaris.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.lunaris.app.ui.components.CreateTaskDialog
import dev.lunaris.app.ui.components.CustomTaskCard
import dev.lunaris.app.ui.theme.ColorPrimary
import dev.lunaris.app.ui.theme.DoneColor
import dev.lunaris.app.ui.theme.InProgressColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navController: NavController){
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tus tareas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
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
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //en progreso
            val inProgress = listOf(
                Triple("Tarea 3", "NOV 02, 2025", "Descripcion 1")
            )
            //completadas
            val done = listOf(
                Triple("Tarea 4", "NOV 02, 2025", "Descripcion 1"),
                Triple("Tarea 5", "NOV 02, 2025", "Descripcion 1")
            )
            Text("En progreso", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(5.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(inProgress) { task ->
                    CustomTaskCard(
                        title = task.first,
                        description = task.third,
                        projectName = "Proyecto 1",
                        listName = "En progreso",
                        date = task.second,
                        InProgressColor,
                        Icons.Default.AddCircle
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("Completadas", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(5.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(done) { task ->
                    CustomTaskCard(
                        title = task.first,
                        description = task.third,
                        projectName = "Proyecto 3",
                        listName = "En progreso",
                        date = task.second,
                        DoneColor,
                        Icons.Default.CheckCircle
                    )
                }
            }
        }
        if (showDialog) {
            CreateTaskDialog(
                onDismiss = { showDialog = false },
                onCreate = { title, description ->
                    //agregar a la lista
                    showDialog = false
                }
            )
        }
    }
}