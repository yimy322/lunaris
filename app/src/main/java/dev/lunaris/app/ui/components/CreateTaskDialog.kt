package dev.lunaris.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.lunaris.app.data.model.User
import dev.lunaris.app.utils.toFormattedDate
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskDialog(
    collaborators: List<User>,
    onDismiss: () -> Unit,
    onCreate: (String, String, Long?, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var assignedUser by remember { mutableStateOf<User?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear nueva tarea") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                //titulo
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título *") },
                    singleLine = true
                )

                //descripcion
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción (opcional)") },
                    maxLines = 3
                )

                //asignado a
                Text("Asignar a")
                Box{
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { dropdownExpanded = true }
                    ) {
                        Text(
                            assignedUser?.name ?: "Selecciona un colaborador"
                        )
                    }
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        collaborators.forEach { user ->
                            DropdownMenuItem(
                                text = { Text("${user.name} (${user.email})") },
                                onClick = {
                                    assignedUser = user
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                //fecha limite
                TextButton(onClick = {
                    showDatePicker = true
                }) {
                    Text(
                        text = if (deadline == null)
                            "Añadir fecha límite"
                        else
                            "Fecha límite: ${deadline!!.toFormattedDate()}"
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank()) {
                    onCreate(title, description, deadline, assignedUser!!.id)
                }
            }) {
                Text("Crear", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    //para seleccionar la fecha
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    val millis = datePickerState.selectedDateMillis
                    deadline = millis
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}