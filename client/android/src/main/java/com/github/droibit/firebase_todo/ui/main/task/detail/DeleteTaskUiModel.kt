package com.github.droibit.firebase_todo.ui.main.task.detail

import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel

data class DeleteTaskUiModel(
    val inProgress: Boolean,
    val success: Event<Unit>?,
    val error: Event<MessageUiModel>?,
)
