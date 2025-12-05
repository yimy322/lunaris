package dev.lunaris.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dev.lunaris.app.data.model.Project
import dev.lunaris.app.data.model.ProjectList
import dev.lunaris.app.data.model.Task
import dev.lunaris.app.data.model.User
import dev.lunaris.app.data.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(
    private val repo: ProjectRepository,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {
    //aca guardaremos los proyectos que obtendremos
    var projects by mutableStateOf<List<Project>>(emptyList())
        private set
    //para manejar estado y errores
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var successMessage by mutableStateOf<String?>(null)
        private set
    var selectedProject by mutableStateOf<Project?>(null)
        private set
    var projectLists: List<ProjectList> by mutableStateOf(emptyList())
        private set
    var tasksByList = mutableStateMapOf<String, List<Task>>()
        private set
    var userSearchResults by mutableStateOf<List<User>>(emptyList())
        private set
    var collaboratorUsers by mutableStateOf<List<User>>(emptyList())
        private set
    var ownerUser by mutableStateOf<User?>(null)
        private set
    //aca concatena las 2 listas
    fun loadProjects() {
        val userId = auth.currentUser?.uid ?: return
        repo.getUserProjectsRealtime(userId) { list ->
            projects = projects + list
            projects = projects.distinctBy { it.id }
        }
    }

    //crear proyecto
    fun createProject(title: String, description: String, color: Color) {
        viewModelScope.launch {
            isLoading = true
            val hex = String.format("#%08X", color.toArgb())
            //mapeamos al objeto
            val project = Project(
                id = "",
                ownerId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                title = title,
                description = description,
                colorHex = hex
            )
            //guardamos
            val result = repo.createProject(project)
            isLoading = false
            //seteamos mensajes
            if (result.isSuccess) {
                successMessage = "Proyecto creado correctamente"
                loadProjects()//para refrescar de nuevo los proyectos
            } else {
                errorMessage = result.exceptionOrNull()?.localizedMessage
            }
        }
    }
    //para buscar por id
    fun loadProjectById(id: String) {
        viewModelScope.launch {
            val result = repo.getProjectById(id)
            result.onSuccess { project ->
                selectedProject = project
                //cargar listas
                projectLists = repo.getListsByProjectId(id)
                //cargar tareas de cada lista
                tasksByList = mutableStateMapOf()
                projectLists.forEach { list ->
                    val tasks = repo.getTasksByListId(list.id)
                    tasksByList[list.id] = tasks
                }
                //cargar colaboradores
                collaboratorUsers = project.collaborators.mapNotNull { uid ->
                    repo.getUserById(uid)
                }
                ownerUser = repo.getUserById(project.ownerId)
            }.onFailure { e ->
                errorMessage = e.message
            }
        }
    }
    //crear lista
    fun createList(projectId: String, title: String) {
        viewModelScope.launch {
            isLoading = true
            //mapeamos al objeto
            val list = ProjectList(
                id = "",
                projectId = projectId,
                title = title
            )
            //guardamos
            val result = repo.createList(list)
            isLoading = false
            //seteamos mensajes
            if (result.isSuccess) {
                successMessage = "Lista creada correctamente"
                projectLists = repo.getListsByProjectId(projectId)//para refrescar las listas
            } else {
                errorMessage = result.exceptionOrNull()?.localizedMessage
            }
        }
    }
    //crear tarea
    fun createTask(
        listId: String,
        title: String,
        description: String,
        deadline: Long?,
        assignedTo: String
    ) {
        viewModelScope.launch {
            val user = auth.currentUser ?: return@launch
            val task = Task(
                listId = listId,
                title = title,
                description = description,
                assignedTo = assignedTo,
                deadline = deadline,
                done = false,
                createdAt = System.currentTimeMillis()
            )

            val result = repo.createTask(task)
            result.onSuccess {
                successMessage = "Tarea creada"
                //volvemos a cargar las tareas
                val tasks = repo.getTasksByListId(listId)
                tasksByList[listId] = tasks
            }.onFailure { e ->
                errorMessage = e.message
            }
        }
    }
    //cambia estado de la tarea
    fun toggleTaskState(listId: String, taskId: String, newState: Boolean) {
        viewModelScope.launch {
            val result = repo.updateTaskState(listId, taskId, newState)
            if (result.isSuccess) {
                loadTasksForLists(projectLists) // recargar tareas
                successMessage = if (newState) "Tarea completada" else "Tarea marcada como pendiente"
            } else {
                errorMessage = "Error al actualizar tarea"
            }
        }
    }
    //actualziar tareas
    private suspend fun loadTasksForLists(lists: List<ProjectList>) {
        lists.forEach { list ->
            val tasks = repo.getTasksByListId(list.id)
            tasksByList[list.id] = tasks
        }
    }
    //limpiamos mensajes
    fun clearMessages() {
        successMessage = null
        errorMessage = null
    }
    //agregar colab
    fun addCollaborator(projectId: String, collaboratorId: String) {
        viewModelScope.launch {
            val project = selectedProject ?: return@launch
            //para que agregue de colab a un colab ya existente
            if (project.collaborators.contains(collaboratorId)) {
                errorMessage = "Este usuario ya es colaborador del proyecto"
                return@launch
            }
            val result = repo.addCollaborator(projectId, collaboratorId)
            if (result.isSuccess) {
                successMessage = "Colaborador agregado"
                loadProjectById(projectId) //refrescamos
            } else {
                errorMessage = "No se pudo agregar el colaborador"
            }
        }
    }
    //buscar usuarios por email
    fun searchUsersByEmail(query: String) {
        repo.searchUsersByEmailPrefix(query) { results ->
            userSearchResults = results
        }
    }
}
