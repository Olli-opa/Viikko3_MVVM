package com.example.viikkotehtava1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viikkotehtava1.model.Task
import com.example.viikkotehtava1.viewmodel.FilterState
import com.example.viikkotehtava1.viewmodel.SortState
import com.example.viikkotehtava1.viewmodel.TaskViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: TaskViewModel = viewModel()) {
    val taskList by viewModel.tasks.collectAsState()
    var newTaskTitle by remember { mutableStateOf("") }
    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Task Manager",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("Enter a new task") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newTaskTitle.isNotBlank()) {
                    val newId = (taskList.maxOfOrNull { it.id } ?: 0) + 1
                    viewModel.addTask(Task(id = newId, title = newTaskTitle, description = "", priority = 1, dueDate = "2025-12-31", done = false))
                    newTaskTitle = ""
                }
            }) {
                Text("Add")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { viewModel.setFilter(FilterState.ALL) }) { Text("All") }
            Button(onClick = { viewModel.setFilter(FilterState.DONE) }) { Text("Done") }
            Button(onClick = { viewModel.setFilter(FilterState.NOT_DONE) }) { Text("Not Done") }
            Button(onClick = { viewModel.setSort(SortState.BY_DUE_DATE) }) { Text("Sort") }
        }

        LazyColumn {
            items(taskList) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { // Open detail view on click
                            selectedTask = task
                            showDetailDialog = true
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.done,
                        onCheckedChange = { viewModel.toggleDone(task.id) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.title,
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { viewModel.removeTask(task.id) }) {
                        Text("Remove")
                    }
                }
            }
        }

        if (showDetailDialog && selectedTask != null) {
            DetailScreen(
                task = selectedTask!!,
                onDismiss = { showDetailDialog = false },
                onUpdateTask = {
                    viewModel.updateTask(it)
                    showDetailDialog = false
                },
                onRemoveTask = {
                    viewModel.removeTask(it)
                    showDetailDialog = false
                }
            )
        }
    }
}