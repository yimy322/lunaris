package dev.lunaris.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.lunaris.app.ui.components.CreateListDialog
import dev.lunaris.app.ui.components.ProjectInfoSection
import dev.lunaris.app.ui.components.ProjectListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(navController: NavController){
    //detalle del proyecto
    val projectTitle = "Proyecto X"
    val projectDescription = "Descripción del proyecto... Aquí puedes mostrar más datos."
    val projectColor = Color(0xFF8FC5FF)
    //listas del proyecto
    var lists by remember { mutableStateOf(listOf("Lista 1", "Tareas importantes")) }
    //estado del dialog
    var showListDialog by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Proyecto X") },
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
                    TextButton(onClick = { showListDialog = true }) {
                        Text("+ Lista")
                    }
                }
            )
        })
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            //detalle del proyecto
            ProjectInfoSection(projectTitle, projectDescription, projectColor)

            Spacer(Modifier.height(24.dp))

            //listas del proyecto
            Text(
                "Listas del proyecto:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            lists.forEach { listName ->
                ProjectListItem(listName)
                Spacer(Modifier.height(8.dp))
            }

            //dialogo
            if (showListDialog) {
                CreateListDialog(
                    onDismiss = { showListDialog = false },
                    onCreate = { listName ->
                        if (listName.isNotBlank()) {
                            lists = lists + listName   //agrega a la lista
                        }
                        showListDialog = false
                    }
                )
            }
        }
    }
}