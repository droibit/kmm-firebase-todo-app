package com.github.droibit.firebase_todo.ui.main.task.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.droibit.firebase_todo.databinding.ListItemTaskBinding
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.ui.common.CheckableImageView
import javax.inject.Inject

class TaskListAdapter @Inject constructor(
    private val clickListener: ItemClickListener
) :
    ListAdapter<Task, TaskListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent).apply {
            itemView.setOnClickListener {
                clickListener.onItemTaskClick(
                    task = getItem(layoutPosition)
                )
            }
            completedCheckBox.setOnClickListener {
                clickListener.onTaskCheckboxClick(
                    task = getItem(layoutPosition)
                )
            }
        }
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

    class ViewHolder(
        private val binding: ListItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val completedCheckBox: CheckableImageView
            get() = binding.completedCheckBox

        constructor(parent: ViewGroup) : this(
            binding = ListItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        fun bindTo(task: Task) {
            binding.task = task
        }
    }

    interface ItemClickListener {
        fun onItemTaskClick(task: Task)

        fun onTaskCheckboxClick(task: Task)
    }
}
