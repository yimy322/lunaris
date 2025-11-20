package dev.lunaris.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import dev.lunaris.app.data.model.ProjectList
import kotlinx.coroutines.tasks.await

class ProjectListRepository {
    private val db = FirebaseFirestore.getInstance()
    private val listsRef = db.collection("projectLists")

    suspend fun createList(list: ProjectList): Boolean {
        return try {
            val docRef = listsRef.document()
            val listWithId = list.copy(id = docRef.id)

            docRef.set(listWithId).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getListsByProject(projectId: String): List<ProjectList> {
        return try {
            listsRef.whereEqualTo("projectId", projectId)
                .get().await()
                .toObjects(ProjectList::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
