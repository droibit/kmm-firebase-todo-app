package com.github.droibit.firebase_todo.ui.main.task.list.sort

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.droibit.firebase_todo.databinding.ListItemTaskSortingBinding
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.ui.main.task.list.drawableResId
import com.github.droibit.firebase_todo.ui.main.task.list.getString

class TaskSortingAdapter(
    private val currentTaskSorting: TaskSorting,
    private val itemClickListener: (TaskSorting) -> Unit
) : ListAdapter<TaskSorting, TaskSortingAdapter.ViewHolder>(DIFF_CALLBACK) {

    init {
        val items = TaskSorting.Key.values().map {
            TaskSorting(
                key = it,
                order = with(currentTaskSorting) {
                    if (this.key == it) this.order else TaskSorting.Order.DEFAULT
                }
            )
        }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent).apply {
            itemView.setOnClickListener {
                itemClickListener.invoke(getItem(layoutPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val sorting = getItem(position)
        holder.bindTo(
            text = sorting.key.getString(context),
            orderIcon = sorting.takeIf { it.key == currentTaskSorting.key }?.let {
                ContextCompat.getDrawable(context, it.order.drawableResId)
            }
        )
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TaskSorting>() {
            override fun areItemsTheSame(oldItem: TaskSorting, newItem: TaskSorting): Boolean {
                return areContentsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: TaskSorting, newItem: TaskSorting): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(
        private val binding: ListItemTaskSortingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        constructor(parent: ViewGroup) : this(
            binding = ListItemTaskSortingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        fun bindTo(text: String, orderIcon: Drawable?) {
            binding.text.text = text
            binding.order.apply {
                isInvisible = if (orderIcon == null) {
                    true
                } else {
                    setImageDrawable(orderIcon); false
                }
            }
        }
    }
}
