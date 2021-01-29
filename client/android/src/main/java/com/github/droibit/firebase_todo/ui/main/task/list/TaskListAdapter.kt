package com.github.droibit.firebase_todo.ui.main.task.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.droibit.firebase_todo.databinding.ListItemTaskBinding
import com.github.droibit.firebase_todo.shared.model.task.Task
import javax.inject.Inject

class TaskListAdapter @Inject constructor() : ListAdapter<Task, TaskListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).apply {
            root.setOnClickListener {
            }
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(task = getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(private val binding: ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(task: Task) {
            binding.task = task
        }
    }
}