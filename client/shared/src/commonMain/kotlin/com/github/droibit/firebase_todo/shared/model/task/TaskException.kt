package com.github.droibit.firebase_todo.shared.model.task

class TaskException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)