package com.example.doit

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent) // Directs user to settings to grant permission
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)





        adapter = TaskAdapter { task ->
            Log.d("MainActivity", "Task clicked: ${task.id}")
            val intent = Intent(this, UpdateCard::class.java)
            intent.putExtra("task_id", task.id)
            startActivity(intent)
        }



        recyclerView.adapter = adapter

        val taskDao = AppDatabase.getDatabase(application).taskDao()

        taskDao.getTasks().observe(this, Observer { tasks ->
            adapter.submitList(tasks)
        })

        val addButton = findViewById<Button>(R.id.add)
        addButton.setOnClickListener {
            val intent = Intent(this, CreateCard::class.java)
            startActivity(intent)
        }
    }
}
