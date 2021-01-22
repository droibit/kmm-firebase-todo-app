package com.github.droibit.firebase_todo.shared.model.user

data class User(
    val id: String,
    val name: String?,
    val email: String?,
    val photoURL: String?
)