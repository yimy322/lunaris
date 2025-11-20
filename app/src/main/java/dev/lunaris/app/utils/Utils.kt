package dev.lunaris.app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//para formatear la fecha que se guarda en la bd
fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}