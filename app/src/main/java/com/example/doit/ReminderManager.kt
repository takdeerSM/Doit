package com.example.doit

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

class ReminderManager(private val context: Context) {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun setReminder(taskId: Int, executionTime: Long) {
        Log.d("ReminderManager", "Setting reminder for Task ID: $taskId at $executionTime")

        // Create an Intent to trigger the ReminderBroadcastReceiver
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("task_id", taskId)
        }

        // Create a PendingIntent for the alarm
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule the alarm
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            executionTime,
            pendingIntent
        )

        Log.d("ReminderManager", "Reminder set successfully for Task ID: $taskId")
    }

    fun cancelReminder(taskId: Int) {
        Log.d("ReminderManager", "Canceling reminder for Task ID: $taskId")

        val intent = Intent(context, ReminderBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        Log.d("ReminderManager", "Reminder canceled for Task ID: $taskId")
    }
}
