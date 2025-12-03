package dev.lunaris.app.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dev.lunaris.app.data.model.Project
import dev.lunaris.app.data.model.ProjectList
import dev.lunaris.app.data.model.Task
import dev.lunaris.app.data.model.User
import kotlinx.coroutines.tasks.await

class ProjectRepository {
    private val db = FirebaseFirestore.getInstance()
    //obtiene la coleccion, en caso no exista la crea cuando toque este coleccion
    private val projectsRef = db.collection("projects")
    //para las listas
    private val listsRef = FirebaseFirestore.getInstance().collection("projectLists")
    private val tasksRef = FirebaseFirestore.getInstance().collection("tasks")
    private val usersRef = FirebaseFirestore.getInstance().collection("users")
    //crea un proyecto, suspende y espera a que termine
    suspend fun createProject(project: Project): Result<String> {
        return try {
            //para generar el id automaticamente
            val doc = projectsRef.document()
            val newId = doc.id
            //copiamos el proyecto y le asiganomos el id generado
            val newProject = project.copy(id = newId)
            //guardamos
            doc.set(newProject).await()
            Result.success(newId)//retornamos el pk
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //obtenemos los proyectos por el pk parametro
    suspend fun getUserProjects(userId: String): Result<List<Project>> {
        return try {
            //buscamos los proyectos donde el pk param es colaborador
            val snapshot = projectsRef
                .whereArrayContains("collaborators", userId)
                .get()
                .await()
            //buscamos los proyectos donde el pk param es propietario
            val ownProjects = projectsRef
                .whereEqualTo("ownerId", userId)
                .get()
                .await()
            //lo convertimos a un objeto project
            val projects = snapshot.toObjects(Project::class.java) +
                    ownProjects.toObjects(Project::class.java)
            //en caso sea propietario y colan agregar esto .distinctBy { it.id }
            Result.success(projects)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserProjectsRealtime(userId: String, onResult: (List<Project>) -> Unit) {
        // Owner
        val ownerListener = projectsRef
            .whereEqualTo("ownerId", userId)
            .addSnapshotListener { snap, err ->
                if (err != null) return@addSnapshotListener

                val ownerProjects = snap?.toObjects(Project::class.java) ?: emptyList()
                onResult(ownerProjects)
            }
        // Collaborator
        val collabListener = projectsRef
            .whereArrayContains("collaborators", userId)
            .addSnapshotListener { snap, err ->
                if (err != null) return@addSnapshotListener

                val collabProjects = snap?.toObjects(Project::class.java) ?: emptyList()
                onResult(collabProjects)
            }
    }

    //actualizar proyecto
    suspend fun updateProject(project: Project): Result<Unit> {
        return try {
            projectsRef.document(project.id).set(project).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //eliminar proyecto
    suspend fun deleteProject(projectId: String): Result<Unit> {
        return try {
            projectsRef.document(projectId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //buscar por pk
    suspend fun getProjectById(id: String): Result<Project> {
        return try {
            val doc = projectsRef.document(id).get().await()
            val project = doc.toObject(Project::class.java)
            if (project != null) {
                Result.success(project)
            } else {
                Result.failure(Exception("Proyecto no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //para obtener las listas del proyecto
    suspend fun getListsByProjectId(projectId: String): List<ProjectList> {
        val snapshot = listsRef.whereEqualTo("projectId", projectId).get().await()
        return snapshot.toObjects(ProjectList::class.java)
    }
    //para crear la lista
    suspend fun createList(projectList: ProjectList): Result<String> {
        return try {
            //para generar el id automaticamente
            val doc = listsRef.document()
            val newId = doc.id
            //copiamos el proyecto y le asiganomos el id generado
            val newProjectList = projectList.copy(id = newId)
            //guardamos
            doc.set(newProjectList).await()
            Result.success(newId)//retornamos el pk
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //para crear la tarea
    suspend fun createTask(task: Task): Result<String> {
        return try {
            val doc = tasksRef.document()
            val newId = doc.id
            val newTask = task.copy(id = newId)
            doc.set(newTask).await()
            Result.success(newId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //cambiar estado de la tarea
    suspend fun updateTaskState(listId: String, taskId: String, isDone: Boolean): Result<Unit> {
        return try {
            tasksRef.document(taskId)
                .update("done", isDone)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // Obtener tareas por listId
    suspend fun getTasksByListId(listId: String): List<Task> {
        val snapshot = tasksRef.whereEqualTo("listId", listId).get().await()
        return snapshot.toObjects(Task::class.java)
    }
    //agregar colaborador a un proyecto
    suspend fun addCollaborator(projectId: String, collaboratorId: String): Result<Unit> {
        return try {
            projectsRef.document(projectId)
                .update("collaborators", FieldValue.arrayUnion(collaboratorId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    //buscar usuarios
    fun searchUsersByEmailPrefix(prefix: String, onResult: (List<User>) -> Unit) {
        if (prefix.isBlank()) {
            onResult(emptyList())
            return
        }
        usersRef
            .orderBy("email")
            .startAt(prefix)
            .endAt(prefix + "\uf8ff")
            .addSnapshotListener { snap, err ->
                if (err != null) return@addSnapshotListener
                val users = snap?.toObjects(User::class.java) ?: emptyList()
                onResult(users)
            }
    }
    //buscar usuario
    suspend fun getUserById(uid: String): User? {
        return try {
            val doc = FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .await()
            doc.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

}