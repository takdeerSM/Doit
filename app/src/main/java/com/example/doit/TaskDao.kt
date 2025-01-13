package com.example.doit

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("""
    SELECT * FROM tasks 
    ORDER BY 
        CASE 
            WHEN priority = 'High' THEN 1 
            WHEN priority = 'Medium' THEN 2 
            WHEN priority = 'Low' THEN 3 
            ELSE 4 
        END, 
        dueDate ASC
""")
    fun getTasks(): LiveData<List<Task>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)


    @Query("DELETE FROM tasks")
    fun deleteAllTasks()

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): Task?




}
