package com.example.viikkotehtava1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viikkotehtava1.domain.Task
import com.example.viikkotehtava1.domain.addTask
import com.example.viikkotehtava1.domain.filterByDone
import com.example.viikkotehtava1.domain.mockTasks
import com.example.viikkotehtava1.domain.sortByDueDate
import com.example.viikkotehtava1.domain.toggleDone
import com.example.viikkotehtava1.ui.theme.ViikkoTehtava1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViikkoTehtava1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var taskList by remember { mutableStateOf(mockTasks) }

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
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val newTask = Task(9, "New Task", "New Description", 1, "2027-01-01", false)
                taskList = addTask(taskList, newTask)
            }) {
                Text("AddNewTask")
            }
            Button(onClick = { taskList = filterByDone(taskList, done = true) }) {
                Text("FilterByDone")
            }
            Button(onClick = { taskList = sortByDueDate(taskList) }) {
                Text("SortByDate")
            }
        }
        LazyColumn {
            items(taskList) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Title: ${task.title}\nDue: ${task.dueDate}\nDone: ${task.done}",
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { taskList = toggleDone(taskList, task.id) }) {
                        Text("Toggle Done")
                    }
                }
            }
        }
    }
}