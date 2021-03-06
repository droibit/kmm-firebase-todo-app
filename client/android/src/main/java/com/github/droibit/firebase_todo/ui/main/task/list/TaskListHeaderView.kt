package com.github.droibit.firebase_todo.ui.main.task.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors

class TaskListHeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val filterTaskButton: MaterialButton
    private val sortTaskButton: MaterialButton

    var onClickListener: OnClickListener? = null

    init {
        View.inflate(context, R.layout.view_task_list_header, this)

        setBackgroundColor(MaterialColors.getColor(this, R.attr.colorSurface))

        filterTaskButton = findViewById(R.id.filterTaskButton)
        filterTaskButton.setOnClickListener {
            onClickListener?.onFilterTaskClick()
        }
        sortTaskButton = findViewById(R.id.sortTaskButton)
        sortTaskButton.setOnClickListener {
            onClickListener?.onChangeSortKeyClick()
        }

        setTaskFilter(TaskFilter.DEFAULT)
        setTaskSorting(TaskSorting.DEFAULT)
    }

    fun setTaskFilter(taskFilter: TaskFilter) {
        filterTaskButton.text = taskFilter.getString(context)
    }

    fun setTaskSorting(taskSorting: TaskSorting) {
        sortTaskButton.text = taskSorting.key.getString(context)
        sortTaskButton.setIconResource(taskSorting.order.drawableResId)
    }

    interface OnClickListener {
        fun onFilterTaskClick()

        fun onChangeSortKeyClick()
    }
}

private fun TaskFilter.getString(context: Context): String {
    val resId = when (this) {
        TaskFilter.ALL -> R.string.task_list_header_all
        TaskFilter.ACTIVE -> R.string.task_list_header_active
        TaskFilter.COMPLETED -> R.string.task_list_header_completed
    }
    return context.getString(resId)
}
