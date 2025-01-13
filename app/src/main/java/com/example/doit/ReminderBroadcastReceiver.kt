package com.example.doit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getIntExtra("task_id", -1)
        Log.d("ReminderBroadcastReceiver", "Received broadcast for Task ID: $taskId")

        if (taskId != -1) {
            val notification = NotificationCompat.Builder(context, "task_channel_id")
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Use a valid icon
                .setContentTitle("Task Reminder")
                .setContentText("You have a task to do!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(taskId, notification)

            Log.d("ReminderBroadcastReceiver", "Notification displayed for Task ID: $taskId")
        } else {
            Log.e("ReminderBroadcastReceiver", "Invalid Task ID received in broadcast")
        }
    }
}
