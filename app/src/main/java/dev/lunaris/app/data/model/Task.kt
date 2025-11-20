package dev.lunaris.app.data.model

data class Task(
    val id: String = "",
    val listId: String = "",
    val title: String = "",
    val description: String = "",
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val deadline: Long? = null
)
