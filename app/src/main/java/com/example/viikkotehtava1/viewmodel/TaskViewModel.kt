package com.example.viikkotehtava1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viikkotehtava1.model.Task
import com.example.viikkotehtava1.model.mockTasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class FilterState { ALL, DONE, NOT_DONE }
enum class SortState { NONE, BY_DUE_DATE }

class TaskViewModel : ViewModel() {

    private val _allTasks = MutableStateFlow(mockTasks)
    private val _filterState = MutableStateFlow(FilterState.ALL)
    private val _sortState = MutableStateFlow(SortState.NONE)

    val tasks: StateFlow<List<Task>> = combine(_allTasks, _filterState, _sortState) { allTasks, filter, sort ->
        val filteredList = when (filter) {
            FilterState.ALL -> allTasks
            FilterState.DONE -> allTasks.filter { it.done }
            FilterState.NOT_DONE -> allTasks.filter { !it.done }
        }
        when (sort) {
            SortState.NONE -> filteredList
            SortState.BY_DUE_DATE -> filteredList.sortedBy { it.dueDate }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = mockTasks
    )

    fun addTask(task: Task) {
        _allTasks.value = _allTasks.value + task
    }

    fun toggleDone(id: Int) {
        _allTasks.value = _allTasks.value.map {
            if (it.id == id) it.copy(done = !it.done) else it
        }
    }

    fun removeTask(id: Int) {
        _allTasks.value = _allTasks.value.filter { it.id != id }
    }

    fun updateTask(updatedTask: Task) {
        _allTasks.value = _allTasks.value.map {
            if (it.id == updatedTask.id) updatedTask else it
        }
    }

    fun setFilter(filter: FilterState) {
        _filterState.value = filter
    }

    fun setSort(sort: SortState) {
        _sortState.value = if (_sortState.value == sort) SortState.NONE else sort
    }
}