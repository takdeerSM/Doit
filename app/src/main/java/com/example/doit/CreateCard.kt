package com.example.doit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CreateCard : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private val cal: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_card)

        val taskDao = AppDatabase.getDatabase(application).taskDao()
        val repository = TaskRepository(taskDao)
        val viewModelFactory = TaskViewModelFactory(repository)
        taskViewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        val titleEditText = findViewById<EditText>(R.id.create_title)
        val priorityEditText = findViewById<EditText>(R.id.create_priority)
        dateEditText = findViewById(R.id.create_date)
        timeEditText = findViewById(R.id.execution_time)
        val datePickerButton = findViewById<Button>(R.id.date_picker_button)
        val timePickerButton = findViewById<Button>(R.id.time_picker_button)
        val saveButton = findViewById<Button>(R.id.save_button)

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

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val priority = priorityEditText.text.toString().trim()
            val executionTime = cal.timeInMillis

            if (title.isEmpty() || priority.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (executionTime < System.currentTimeMillis()) {
                Toast.makeText(this, "Please set a future time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val task = Task(
                title = title,
                description = "",
                priority = priority,
                dueDate = System.currentTimeMillis(),
                executionTime = executionTime
            )

            taskViewModel.insert(task) {
                scheduleNotification(task.id, title, executionTime) // Schedule notification
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun scheduleNotification(taskId: Int, title: String, executionTimeMillis: Long) {
        val delay = executionTimeMillis - System.currentTimeMillis()

        if (delay <= 0) {
            Toast.makeText(this, "Cannot schedule a notification in the past", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Data.Builder()
            .putString("task_title", title)
            .putInt("task_id", taskId)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
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
}
