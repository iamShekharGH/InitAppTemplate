package com.shekhargh.reminderApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shekhargh.reminderApp.ui.homeScreen.HomeScreenComposable
import com.shekhargh.reminderApp.ui.theme.ReminderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReminderTheme {
                HomeScreenComposable()
            }
        }
    }
}