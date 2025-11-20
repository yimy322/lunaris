package dev.lunaris.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.lunaris.app.data.repository.ProjectRepository
import dev.lunaris.app.ui.components.CreateListDialog
import dev.lunaris.app.ui.components.ProjectInfoSection
import dev.lunaris.app.ui.components.ProjectListItem
import dev.lunaris.app.utils.toFormattedDate
import dev.lunaris.app.viewmodel.ProjectViewModel
import dev.lunaris.app.viewmodel.ProjectViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(navController: NavController, projectId: String){
    val repo = ProjectRepository()
    val factory = ProjectViewModelFactory(repo)
    val viewModel: ProjectViewModel = viewModel(factory = factory)

    LaunchedEffect(projectId) {
        viewModel.loadProjectById(projectId)
    }

    val project = viewModel.selectedProject

    if (project == null) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        return
    }
    //listas del proyecto
    val lists = viewModel.projectLists
    //estado del dialog
    var showListDialog by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(project.title) },
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
            ProjectInfoSection(
                project.title,
                project.description,
                Color(project.colorHex.toColorInt()),
                "Creado el: ${project.createdAt.toFormattedDate()}")

            Spacer(Modifier.height(24.dp))

            //listas del proyecto
            Text(
                "Listas del proyecto:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))
            if (lists.isEmpty()) {
                Text(
                    text = "Este proyecto aún no tiene listas",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }else{
                lists.forEach { item ->
                    ProjectListItem(item.title)
                    Spacer(Modifier.height(8.dp))
                }
            }
            //dialogo
            if (showListDialog) {
                CreateListDialog(
                    onDismiss = { showListDialog = false },
                    onCreate = { title ->
                        if (title.isNotBlank()) {
                            //viewModel.addListToProject(projectId, title)
                        }
                        showListDialog = false
                    }
                )
            }
        }
    }
}