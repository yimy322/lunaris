package dev.lunaris.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.lunaris.app.ui.components.CreateProjectDialog
import dev.lunaris.app.ui.components.CustomProjectCard
import dev.lunaris.app.ui.theme.ColorPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(navController: NavController){
    //para el dialog de crear proyecto
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tus proyectos",
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
            val projects = listOf(
                Triple("Proyecto 1", "NOV 02, 2025", Color(0xFF8FC5FF)),
                Triple("Proyecto 2", "NOV 02, 2025", Color(0xFFE3B5FF)),
                Triple("Proyecto 3", "NOV 02, 2025", Color(0xFF74D788)),
                Triple("Proyecto 4", "NOV 02, 2025", Color(0xFF8FC5FF)),
                Triple("Proyecto 5", "NOV 02, 2025", Color(0xFFE3B5FF)),
                Triple("Proyecto 6", "NOV 02, 2025", Color(0xFF74D788))
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(projects) { project ->
                    CustomProjectCard(
                        title = project.first,
                        date = project.second,
                        description = "Mi proyecto",
                        barColor = project.third
                    )
                }
            }
        }
        if (showDialog) {
            CreateProjectDialog(
                onDismiss = { showDialog = false },
                onCreate = { title, description, color ->
                    //agregar a la lista
                    showDialog = false
                }
            )
        }

    }
}