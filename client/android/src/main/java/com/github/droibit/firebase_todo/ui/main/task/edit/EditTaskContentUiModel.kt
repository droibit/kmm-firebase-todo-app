package com.github.droibit.firebase_todo.ui.main.task.edit

import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel

data class EditTaskContentUiModel(
    val inProgress: Boolean,
    val success: Event<MessageUiModel>?,
    val error: Event<MessageUiModel>?,
)