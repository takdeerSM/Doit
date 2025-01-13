package com.example.doit

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val taskTitle = inputData.getString("task_title") ?: "Task Reminder"
        val taskId = inputData.getInt("task_id", 0)

        Log.d("NotificationWorker", "Executing worker for Task ID: $taskId with title: $taskTitle")

        // Create a notification channel
        createNotificationChannel()

        // Build and display the notification
        val notification = NotificationCompat.Builder(applicationContext, "task_channel_id")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Task Reminder")
            .setContentText("You have a task: $taskTitle")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(taskId, notification)

        Log.d("NotificationWorker", "Notification displayed for Task ID: $taskId")
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_channel_id",
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
            Log.d("NotificationWorker", "Notification channel created")
        }
    }
}
