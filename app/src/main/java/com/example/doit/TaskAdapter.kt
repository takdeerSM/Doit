package com.example.doit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Color
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(private val onItemClick: (Task) -> Unit) :
    ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task, onItemClick: (Task) -> Unit) {
            val titleTextView = itemView.findViewById<TextView>(R.id.title)
            val priorityTextView = itemView.findViewById<TextView>(R.id.priority)
            val executionTimeTextView = itemView.findViewById<TextView>(R.id.execution_time)
            val cardView = itemView.findViewById<CardView>(R.id.task_card) // Updated to use CardView

            // Set task details
            titleTextView.text = task.title
            priorityTextView.text = task.priority

            // Format and set the execution time
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            executionTimeTextView.text = sdf.format(task.executionTime)

            // Set background color based on priority
            val backgroundColor = when (task.priority.lowercase(Locale.getDefault())) {
                "high" -> Color.RED
                "medium" -> Color.YELLOW
                "low" -> Color.GREEN
                else -> Color.GRAY
            }
            cardView.setCardBackgroundColor(backgroundColor) // Apply color to CardView

            // Set click listener
            itemView.setOnClickListener { onItemClick(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
