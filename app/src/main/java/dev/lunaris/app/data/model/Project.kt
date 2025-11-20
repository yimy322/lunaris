package dev.lunaris.app.data.model

data class Project(
    val id: String = "",
    val ownerId: String = "",
    val collaborators: List<String> = emptyList(),
    val title: String = "",
    val description: String = "",
    val colorHex: String = "#FF8FC5FF",//color por defecto
    val createdAt: Long = System.currentTimeMillis()
)
