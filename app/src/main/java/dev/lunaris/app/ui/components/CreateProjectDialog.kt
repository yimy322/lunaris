package dev.lunaris.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.lunaris.app.ui.theme.ProjectColor1
import dev.lunaris.app.ui.theme.ProjectColor2
import dev.lunaris.app.ui.theme.ProjectColor3
import dev.lunaris.app.ui.theme.ProjectColor4
import dev.lunaris.app.ui.theme.ProjectColor5

@Composable
fun CreateProjectDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, Color) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    //sacar colores de la bd
    val colors = listOf(
        ProjectColor1,
        ProjectColor2,
        ProjectColor3,
        ProjectColor4,
        ProjectColor5
    )
    var selectedColor by remember { mutableStateOf(colors[0]) }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank()) {
                    onCreate(title, description, selectedColor)
                }
            }) {
                Text("Crear", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Crear nuevo proyecto") },

        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                //titulo
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título del proyecto") },
                    singleLine = true
                )

                //descripcion
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    maxLines = 3
                )

                //seleccion de color
                Text("Color del proyecto:", fontWeight = FontWeight.SemiBold)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (color == selectedColor) 3.dp else 1.dp,
                                    color = if (color == selectedColor) Color.Black else Color.Gray,
                                    shape = CircleShape
                                )
                                .clickable{ selectedColor = color }
                        )
                    }
                }
            }
        }
    )
}