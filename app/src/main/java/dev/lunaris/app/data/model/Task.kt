package dev.lunaris.app.data.model

import com.google.firebase.firestore.PropertyName

data class Task(
    val id: String = "",
    val listId: String = "",
    val title: String = "",
    val description: String = "",
    val assignedTo: String = "",
    val done: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val deadline: Long? = null
)
