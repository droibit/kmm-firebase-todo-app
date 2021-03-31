package com.github.droibit.firebase_todo.shared.model.task

import com.github.droibit.firebase_todo.shared.utils.Parcelable
import com.github.droibit.firebase_todo.shared.utils.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Workaround for deserialize an ID as part of the object using Firestore.
// ref. https://github.com/GitLiveApp/firebase-kotlin-sdk/issues/39
@Parcelize
@Serializable
data class Task(
    val id: String = "",
    val title: String,
    val description: String,
    @SerialName("completed") val isCompleted: Boolean,
    val createdAt: Double,
    val updatedAt: Double,
) : Parcelable {
    val isActive: Boolean get() = !isCompleted
}