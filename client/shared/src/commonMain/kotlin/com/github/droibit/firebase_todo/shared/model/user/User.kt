package com.github.droibit.firebase_todo.shared.model.user

data class User(
    val uid: String,
    val name: String?,
    val email: String?,
    val photoURL: String?
)