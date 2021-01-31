package com.github.droibit.firebase_todo.ui.main.task.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter

class TaskListHeaderView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val taskFilterButton: Button
    private val changeSortKeyButton: Button
    private val toggleSortOrderButton: ImageView

    init {
        View.inflate(context, R.layout.view_task_list_header, this)

        taskFilterButton = findViewById(R.id.taskFilterButton)
        changeSortKeyButton = findViewById(R.id.changeSortKeyButton)
        toggleSortOrderButton = findViewById(R.id.toggleSortOrderButton)

        taskFilterButton.text = TaskFilter.DEFAULT.getString(context)
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