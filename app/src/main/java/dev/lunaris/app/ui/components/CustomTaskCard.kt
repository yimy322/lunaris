package dev.lunaris.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lunaris.app.ui.theme.ColorPrimary
import dev.lunaris.app.ui.theme.ColorSecondary

@Composable
fun CustomTaskCard(
    title: String,
    description: String,
    projectName: String,
    listName: String,
    date: String,
    assignedUserName: String,
    assignedUserEmail: String,
    iconColor: Color,
    imageVector: ImageVector,
    canToggle: Boolean = true,
    onToggleState: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    color = Color(0xFF6E6E6E),
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { if (canToggle) onToggleState?.invoke() },
                    enabled = canToggle
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = "Cambiar estado",
                        tint = if (canToggle) iconColor else Color.Gray.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(description, fontSize = 12.sp, color = Color.Gray)

            //nombre de la lista
            Spacer(modifier = Modifier.height(2.dp))
            Text(listName, fontSize = 12.sp, color = iconColor, fontWeight = FontWeight.SemiBold)

            //fecha limite
            Spacer(modifier = Modifier.height(8.dp))
            Text(date, fontSize = 12.sp, color = Color.Gray)

            //colaborador
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = assignedUserName.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = assignedUserName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF505050),
                        lineHeight = 12.sp
                    )
                    Text(
                        text = assignedUserEmail,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        lineHeight = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(iconColor)
                    .padding(vertical = 2.dp, horizontal = 12.dp)
            ) {
                Text(
                    //nombre del proyecto
                    text = projectName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
