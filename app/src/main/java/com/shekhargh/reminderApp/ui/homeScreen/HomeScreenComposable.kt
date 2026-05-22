package com.shekhargh.reminderApp.ui.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shekhargh.reminderApp.data.db.Priority
import com.shekhargh.reminderApp.data.db.Reminder
import com.shekhargh.reminderApp.ui.theme.ReminderTheme
import com.shekhargh.reminderApp.ui.theme.getPriorityColor
import com.shekhargh.reminderApp.util.SampleData
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenComposable() {
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val uiState by viewModel.homeScreenState.collectAsStateWithLifecycle()

    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    HomeScreenContent(
        uiState = uiState,
        onReminderClick = { selectedReminder = it },
        onCompletionChange = { reminder, isCompleted ->
            viewModel.updateCompletionStatus(reminder, isCompleted)
        }
    )

    if (selectedReminder != null) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    selectedReminder = null
                }
            },
            sheetState = sheetState
        ) {
            ReminderEditBottomSheet(
                reminder = selectedReminder!!,
                onSave = { updatedReminder ->
                    viewModel.saveReminder(updatedReminder)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        selectedReminder = null
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeScreenUiState,
    onReminderClick: (Reminder) -> Unit = {},
    onCompletionChange: (Reminder, Boolean) -> Unit = { _, _ -> }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Reminders",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Navigate to Add Reminder */ },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is HomeScreenUiState.Items -> {
                    if (uiState.items.isEmpty()) {
                        Text(
                            "No reminders yet. Tap + to add one!",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(items = uiState.items, key = { it.id }) { item ->
                                ReminderListItem(
                                    reminder = item,
                                    onClick = { onReminderClick(item) },
                                    onCompletionChange = { onCompletionChange(item, it) }
                                )
                            }
                        }
                    }
                }

                HomeScreenUiState.Loading -> {
                    CircularProgressIndicator()
                }

                HomeScreenUiState.Error -> {
                    Text(
                        text = "Something went wrong!",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                HomeScreenUiState.Initial -> {
                    Text("Ready to go!", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ReminderTheme {
        HomeScreenContent(
            uiState = HomeScreenUiState.Items(SampleData.reminders)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditBottomSheetPreview() {
    ReminderTheme {
        ReminderEditBottomSheet(
            reminder = SampleData.reminders[0],
            onSave = {}
        )
    }
}
