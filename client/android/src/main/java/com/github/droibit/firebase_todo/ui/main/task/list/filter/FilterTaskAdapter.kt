package com.github.droibit.firebase_todo.ui.main.task.list.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.ui.common.CheckableSingleLineViewHolder

class FilterTaskAdapter(
    private val currentTaskFilter: TaskFilter,
    private val itemClickListener: (TaskFilter) -> Unit
) : ListAdapter<TaskFilter, CheckableSingleLineViewHolder>(DIFF_CALLBACK) {

    init {
        submitList(TaskFilter.values().toList())
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckableSingleLineViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_checkable_single_line, parent, false)
        return CheckableSingleLineViewHolder(itemView).apply {
            itemView.setOnClickListener {
                itemClickListener.invoke(getItem(layoutPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: CheckableSingleLineViewHolder, position: Int) {
        val textFilter = getItem(position)
        holder.bindTo(
            text = textFilter.getString(holder.itemView.context),
            checked = currentTaskFilter == textFilter
        )
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TaskFilter>() {
            override fun areItemsTheSame(oldItem: TaskFilter, newItem: TaskFilter): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TaskFilter, newItem: TaskFilter): Boolean {
                return oldItem == newItem
            }
        }
    }
}

private fun TaskFilter.getString(context: Context): String {
    val resId = when (this) {
        TaskFilter.ALL -> R.string.task_list_filter_all
        TaskFilter.ACTIVE -> R.string.task_list_filter_active
        TaskFilter.COMPLETED -> R.string.task_list_filter_completed
    }
    return context.getString(resId)
}
