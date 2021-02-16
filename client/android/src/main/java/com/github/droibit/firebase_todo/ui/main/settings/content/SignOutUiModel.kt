package com.github.droibit.firebase_todo.ui.main.settings.content

import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel

data class SignOutUiModel(
    val inProgress: Boolean,
    val success: Event<Unit>?,
    val error: Event<MessageUiModel>?,
)
