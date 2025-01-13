package com.example.doit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class UpdateCard : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var priorityEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var taskDao: TaskDao
    private val cal: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_card)

        titleEditText = findViewById(R.id.create_title)
        priorityEditText = findViewById(R.id.create_priority)
        dateEditText = findViewById(R.id.create_date)
        timeEditText = findViewById(R.id.execution_time)
        val updateButton = findViewById<Button>(R.id.update_button)
        val deleteButton = findViewById<Button>(R.id.delete_button)
        val datePickerButton = findViewById<Button>(R.id.date_picker_button)
        val timePickerButton = findViewById<Button>(R.id.time_picker_button)

        taskDao = AppDatabase.getDatabase(application).taskDao()
        val taskId = intent.getIntExtra("task_id", -1)

        if (taskId == -1) {
            Toast.makeText(this, "Invalid Task ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchAndPopulateTask(taskId)

        datePickerButton.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        timePickerButton.setOnClickListener {
            TimePickerDialog(this, { _, hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                updateTimeInView()
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        updateButton.setOnClickListener {
            updateTask(taskId)
        }

        deleteButton.setOnClickListener {
            deleteTask(taskId)
        }
    }

    private fun fetchAndPopulateTask(taskId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val task = taskDao.getTaskById(taskId)
            withContext(Dispatchers.Main) {
                if (task != null) {
                    titleEditText.setText(task.title)
                    priorityEditText.setText(task.priority)
                    cal.timeInMillis = task.executionTime
                    updateDateInView()
                    updateTimeInView()
                } else {
                    Toast.makeText(this@UpdateCard, "Task not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateEditText.setText(sdf.format(cal.time))
    }

    private fun updateTimeInView() {
        val myFormat = "HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        timeEditText.setText(sdf.format(cal.time))
    }

    private fun updateTask(taskId: Int) {
        val updatedTitle = titleEditText.text.toString().trim()
        val updatedPriority = priorityEditText.text.toString().trim()
        val updatedExecutionTime = cal.timeInMillis

        if (updatedTitle.isEmpty() || updatedPriority.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val updatedTask = Task(
                id = taskId,
                title = updatedTitle,
                description = "",
                priority = updatedPriority,
                dueDate = System.currentTimeMillis(),
                executionTime = updatedExecutionTime,
                isComplete = false
            )
            taskDao.updateTask(updatedTask)

            ReminderManager(this@UpdateCard).cancelReminder(taskId)
            ReminderManager(this@UpdateCard).setReminder(taskId, updatedExecutionTime)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@UpdateCard, "Task updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun deleteTask(taskId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            taskDao.deleteTask(Task(id = taskId, title = "", description = "", priority = "", dueDate = 0L, executionTime = 0L, isComplete = false))
            ReminderManager(this@UpdateCard).cancelReminder(taskId)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UpdateCard, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
