package dev.lunaris.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import dev.lunaris.app.viewmodel.ProjectViewModel

@Composable
fun AddCollaboratorDialog(
    viewModel: ProjectViewModel,
    projectId: String,
    onDismiss: () -> Unit
) {
    var emailInput by remember { mutableStateOf("") }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar colaborador") },
        text = {
            Column {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it
                        viewModel.searchUsersByEmail(it)
                    },
                    label = { Text("Correo del colaborador") }
                )
                Spacer(Modifier.height(8.dp))
                val results = viewModel.userSearchResults.filter { it.id != currentUserId }
                if (results.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                    ) {
                        items(results) { user ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.addCollaborator(projectId, user.id)
                                        onDismiss()
                                    }
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(user.name ?: "Sin nombre")
                                    Text(user.email, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

