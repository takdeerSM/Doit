package com.example.doit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
    fun insert(task: Task, onCompleted: () -> Unit) = viewModelScope.launch {
        try {
            withContext(Dispatchers.IO) {
                repository.insert(task)
            }
            withContext(Dispatchers.Main) {
                onCompleted()
            }
        } catch (e: SecurityException) {
            Log.e("TaskViewModel", "Failed to insert task: ${e.message}")
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Unexpected error: ${e.message}")
        }
    }



    fun update(task: Task, onCompleted: () -> Unit) = viewModelScope.launch {
        try {
            withContext(Dispatchers.IO) {
                repository.update(task)
            }
            withContext(Dispatchers.Main) {
                onCompleted()
            }
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Failed to update task", e)
        }
    }

    fun delete(task: Task, onCompleted: () -> Unit) = viewModelScope.launch {
        try {
            withContext(Dispatchers.IO) {
                repository.delete(task)
            }
            withContext(Dispatchers.Main) {
                onCompleted()
            }
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Failed to delete task", e)
        }
    }

}

