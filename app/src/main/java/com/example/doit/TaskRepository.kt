package com.example.doit

import androidx.lifecycle.LiveData


class TaskRepository(private val taskDao: TaskDao) {
    suspend fun insert(task: Task) {
        taskDao.insertTask(task)  // This method should be called from a background thread context
    }
    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }


}

