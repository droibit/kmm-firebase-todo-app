package com.github.droibit.firebase_todo.ui.onboarding.signin

import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel

data class SignInUiModel(
    val inProgress: Boolean,
    val success: Event<Unit>?,
    val error: Event<MessageUiModel>?,
)