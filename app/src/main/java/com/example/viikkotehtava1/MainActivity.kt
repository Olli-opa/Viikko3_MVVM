package com.example.viikkotehtava1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.viikkotehtava1.ui.theme.ViikkoTehtava1Theme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import com.example.viikkotehtava1.domain.mockTasks

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViikkoTehtava1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ParentComponent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun ParentComponent(modifier: Modifier = Modifier) {
    var taskList by remember { mutableStateOf(mockTasks) }

    Column (modifier = modifier) {
        taskList.forEach { task ->
            Text("${task.id} -Title: ${task.title} ${task.description} ${task.dueDate} ${task.done}")
        }
    }
}