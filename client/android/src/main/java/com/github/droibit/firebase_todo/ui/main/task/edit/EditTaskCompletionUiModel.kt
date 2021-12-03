package com.github.droibit.firebase_todo.ui.main.task.edit

import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel

data class EditTaskCompletionUiModel(
    val inProgress: Boolean,
    val success: Event<Unit>?,
    val error: Event<MessageUiModel>?,
)
