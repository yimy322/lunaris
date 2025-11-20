package dev.lunaris.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.lunaris.app.data.repository.ProjectRepository
import dev.lunaris.app.ui.components.CreateListDialog
import dev.lunaris.app.ui.components.CreateTaskDialog
import dev.lunaris.app.ui.components.CustomTaskCard
import dev.lunaris.app.ui.components.ProjectInfoSection
import dev.lunaris.app.ui.components.ProjectListItem
import dev.lunaris.app.ui.theme.ColorSecondary
import dev.lunaris.app.ui.theme.DoneColor
import dev.lunaris.app.utils.toFormattedDate
import dev.lunaris.app.viewmodel.ProjectViewModel
import dev.lunaris.app.viewmodel.ProjectViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(navController: NavController, projectId: String){
    val repo = ProjectRepository()
    val factory = ProjectViewModelFactory(repo)
    val viewModel: ProjectViewModel = viewModel(factory = factory)
    val snackbarHostState = remember { SnackbarHostState() }
    //para mostrar el dialog de crear tarea
    var showTaskDialogForList by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(projectId) {
        viewModel.loadProjectById(projectId)
    }

    //escuchamos los mensajes
    LaunchedEffect(viewModel.successMessage, viewModel.errorMessage) {
        viewModel.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
        viewModel.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    val project = viewModel.selectedProject
    //tareas de la lista
    val tasksByList = viewModel.tasksByList
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
                lists.forEach { list ->
                    //titulo de la lista
                    ProjectListItem(list.title)
                    Spacer(Modifier.height(8.dp))

                    //button para crear la tarea dentro de la lista
                    TextButton(onClick = {
                        showTaskDialogForList = list.id
                    }) {
                        Icon(Icons.Default.AddCircle, contentDescription = null, tint = DoneColor)
                        Text("Agregar tarea", color = DoneColor)
                    }

                    Spacer(Modifier.height(8.dp))

                    //obtenemos las tareas de la lista
                    val tasks = viewModel.tasksByList[list.id] ?: emptyList()

                    if (tasks.isEmpty()) {
                        Text("No hay tareas en esta lista aún.", fontSize = 13.sp, color = Color.Gray)
                    } else {
                        //mostramos tareas en caso haya
                        tasks.forEach { task ->
                            CustomTaskCard(
                                title = task.title,
                                description = task.description,
                                projectName = project.title,
                                listName = list.title,
                                date = "Creado el: ${task.createdAt.toFormattedDate()}",
                                iconColor = ColorSecondary,
                                imageVector = Icons.Default.CheckCircle
                            )

                            Spacer(Modifier.height(12.dp))
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
            //dialogo
            if (showListDialog) {
                CreateListDialog(
                    onDismiss = { showListDialog = false },
                    onCreate = { title ->
                        if (title.isNotBlank()) {
                            viewModel.createList(projectId, title)
                        }
                        showListDialog = false
                    }
                )
            }
            //para el dialogo de crear tareas
            if (showTaskDialogForList != null) {
                CreateTaskDialog(
                    onDismiss = { showTaskDialogForList = null },
                    onCreate = { title, description, deadline ->
                        viewModel.createTask(
                            listId = showTaskDialogForList!!,
                            title = title,
                            description = description,
                            deadline = deadline
                        )
                        showTaskDialogForList = null
                    }
                )
            }

        }
    }
}