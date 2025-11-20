package dev.lunaris.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import dev.lunaris.app.data.model.Task
import kotlinx.coroutines.tasks.await

class TaskRepository {
    private val db = FirebaseFirestore.getInstance()
    private val tasksRef = db.collection("tasks")

    suspend fun createTask(task: Task): Boolean {
        return try {
            val docRef = tasksRef.document()
            val taskWithId = task.copy(id = docRef.id)

            docRef.set(taskWithId).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getTasksByList(listId: String): List<Task> {
        return try {
            tasksRef.whereEqualTo("listId", listId)
                .get().await()
                .toObjects(Task::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
