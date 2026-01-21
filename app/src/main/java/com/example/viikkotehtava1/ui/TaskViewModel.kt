package com.example.viikkotehtava1.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.viikkotehtava1.domain.Task
import com.example.viikkotehtava1.domain.mockTasks

class TaskViewModel : ViewModel() {

    private var _allTasks by mutableStateOf(listOf<Task>())
    private var isSortedByDueDate = false


    var tasks by mutableStateOf(listOf<Task>())
        private set

    init {
        _allTasks = mockTasks
        tasks = _allTasks
    }

    fun addTask(task: Task) {
        _allTasks = _allTasks + task
        tasks = _allTasks
    }

    fun toggleDone(id: Int) {
        _allTasks = _allTasks.map {
            if (it.id == id) it.copy(done = !it.done) else it
        }

        tasks = if (isSortedByDueDate) {
            _allTasks.sortedBy { it.dueDate }
        } else {
            _allTasks
        }
    }

    fun removeTask(id: Int) {
        _allTasks = _allTasks.filter { it.id != id }
        tasks = _allTasks
    }

    fun filterByDone(done: Boolean) {
        tasks = _allTasks.filter { it.done == done }
    }

    fun showAllTasks() {
        tasks = _allTasks
    }

    fun sortByDueDate() {
        isSortedByDueDate = true
        tasks = tasks.sortedBy { it.dueDate }
    }
}