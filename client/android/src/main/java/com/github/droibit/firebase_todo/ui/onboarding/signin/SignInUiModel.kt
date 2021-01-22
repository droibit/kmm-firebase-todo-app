package com.github.droibit.firebase_todo.ui.onboarding.signin

import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.utils.StringResId

data class SignInUiModel(
    val showProgress: Boolean,
    val showError: Event<StringResId>?,
    val showSuccess: Event<Unit>?,
)