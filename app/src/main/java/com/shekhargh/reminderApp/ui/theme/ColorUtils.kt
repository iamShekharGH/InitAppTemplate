package com.shekhargh.reminderApp.ui.theme

import androidx.compose.ui.graphics.Color
import com.shekhargh.reminderApp.data.db.Priority

fun getPriorityColor(priority: Priority): Color {
    return when (priority) {
        Priority.HIGH -> Color(0xFFE57373)
        Priority.MEDIUM -> Color(0xFFFFB74D)
        Priority.LOW -> Color(0xFF81C784)
    }
}
