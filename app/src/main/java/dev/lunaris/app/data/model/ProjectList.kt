package dev.lunaris.app.data.model

data class ProjectList(
    val id: String = "",
    val projectId: String = "",
    val title: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
