package com.github.droibit.firebase_todo.shared.data.source.task

import com.chrynan.inject.Inject
import com.chrynan.inject.Singleton
import com.github.aakira.napier.Napier
import com.github.droibit.firebase_todo.shared.data.source.FirestorePaths
import com.github.droibit.firebase_todo.shared.model.task.Statistics
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskException
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.Query
import dev.gitlive.firebase.firestore.code
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class TaskDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getTaskList(
        userId: String,
        taskFilter: TaskFilter,
        taskSorting: TaskSorting
    ): Flow<List<Task>> {
        return firestore.collection(FirestorePaths.tasks(userId))
            .where(taskFilter)
            .orderBy(taskSorting)
            .limit(100)
            .snapshots
            // Workaround to exclude tasks with null serverTimestamp
            // e.g. Immediately after creating a task.
            .filter { !it.metadata.hasPendingWrites }
            .map { snapshot ->
                snapshot.documents.map {
                    // Workaround for deserialize an ID as part of the object using Firestore.
                    // ref. https://github.com/GitLiveApp/firebase-kotlin-sdk/issues/39
                    it.data(Task.serializer())
                        .copy(id = it.id)
                }
            }
            .catch { error ->
                throw TaskException(cause = error)
            }
    }

    fun getTask(userId: String, taskId: String): Flow<Task?> {
        return firestore.document(FirestorePaths.task(userId, taskId))
            .snapshots
            // Workaround to exclude tasks with null serverTimestamp
            // e.g. Immediately after creating a task.
            .filter { !it.metadata.hasPendingWrites }
            .map {
                if (it.exists) {
                    it.data(Task.serializer()).copy(id = it.id)
                } else {
                    null
                }
            }
            .catch { error ->
                throw TaskException(cause = error)
            }
    }

    private fun Query.where(filter: TaskFilter): Query {
        return when (filter) {
            TaskFilter.ALL -> this
            TaskFilter.COMPLETED -> this.where(TaskFields.COMPLETED, equalTo = true)
            TaskFilter.ACTIVE -> this.where(TaskFields.COMPLETED, equalTo = false)
        }
    }

    private fun Query.orderBy(sorting: TaskSorting): Query {
        // TODO: Case Insensitive Sorting Using Query.
        // ref. https://stackoverflow.com/questions/48096063/cloud-firestore-case-insensitive-sorting-using-query
        return when (sorting.key) {
            TaskSorting.Key.TITLE -> when (sorting.order) {
                TaskSorting.Order.ASC -> {
                    orderBy(TaskFields.TITLE, Direction.ASCENDING)
                        .orderBy(TaskFields.CREATED_AT, Direction.ASCENDING)
                }
                TaskSorting.Order.DESC -> {
                    orderBy(TaskFields.TITLE, Direction.DESCENDING)
                        .orderBy(TaskFields.CREATED_AT, Direction.DESCENDING)
                }
            }
            TaskSorting.Key.CREATED_DATE -> when (sorting.order) {
                TaskSorting.Order.ASC -> {
                    orderBy(TaskFields.CREATED_AT, Direction.ASCENDING)
                        .orderBy(TaskFields.TITLE, Direction.ASCENDING)
                }
                TaskSorting.Order.DESC -> {
                    orderBy(TaskFields.CREATED_AT, Direction.DESCENDING)
                        .orderBy(TaskFields.TITLE, Direction.DESCENDING)
                }
            }
        }
    }

    @Throws(TaskException::class, CancellationException::class)
    suspend fun createTask(userId: String, title: String, description: String) {
        try {
            val tasksRef = firestore.collection(FirestorePaths.tasks(userId))
            val newTask = Task(
                title = title,
                description = description,
                isCompleted = false,
                createdAt = FieldValue.serverTimestamp,
                updatedAt = FieldValue.serverTimestamp
            )

            val result = tasksRef.add(
                Task.serializer(),
                newTask,
                encodeDefaults = false
            )
            Napier.d("Created task: ${result.id}")
        } catch (e: FirebaseFirestoreException) {
            Napier.w("Create task error(${e.code}):", e)
            throw TaskException(cause = e)
        }
    }

    @Throws(TaskException::class, CancellationException::class)
    suspend fun updateTask(userId: String, taskId: String, title: String, description: String) {
        updateTask(userId, taskId,
            UpdateTitleDescription(
                title = title,
                description = description,
            ),
            UpdateTitleDescription.serializer()
        )
    }

    @Throws(TaskException::class, CancellationException::class)
    suspend fun updateTask(userId: String, taskId: String, completed: Boolean) {
        updateTask(userId, taskId,
            UpdateCompleted(isCompleted = completed),
            UpdateCompleted.serializer()
        )
    }

    @Throws(TaskException::class, CancellationException::class)
    private suspend fun <T>updateTask(userId: String, taskId: String, data: T, strategy: SerializationStrategy<T>) {
        try {
            val taskRef = firestore.document(FirestorePaths.task(userId, taskId))
            if (!taskRef.get().exists) {
                throw TaskException("The specified task($taskId) does not exist.")
            }

            taskRef.update(strategy, data, encodeDefaults = true)
        } catch (e: FirebaseFirestoreException) {
            Napier.w("Update task error(${e.code}):", e)
            throw TaskException(cause = e)
        }
    }

    suspend fun deleteTask(userId: String, taskId: String) {
        try {
            val taskRef = firestore.document(FirestorePaths.task(userId, taskId))
            // Deleting a non-existent document will not throw an error.
            taskRef.delete()
        } catch (e: FirebaseFirestoreException) {
            Napier.w("Update task error(${e.code}):", e)
            throw TaskException(cause = e)
        }
    }

    fun getTaskStatistics(userId: String): Flow<Statistics> {
        // TODO: We must deploy firebase functions.
        return firestore.document(FirestorePaths.statistics(userId))
            .snapshots
            .filter { it.exists }
            .map {
                it.data(Statistics.serializer())
            }
            .catch { error ->
                throw TaskException(cause = error)
            }
    }

    internal object TaskFields {
        const val TITLE = "title"
        const val COMPLETED = "completed"
        const val CREATED_AT = "createdAt"
    }
}

/**
 * Workaround for crash when `DocumentReference#update` has values of multiple types.
 * - SerializationException: Serializer for class 'Any' is not found.
 */
@Serializable
private class UpdateTitleDescription(
    val title: String,
    val description: String,
    val updatedAt: Double = FieldValue.serverTimestamp
)

/**
 * Workaround for crash when `DocumentReference#update` has values of multiple types.
 * - SerializationException: Serializer for class 'Any' is not found.
 */
@Serializable
private class UpdateCompleted(
    @SerialName("completed") val isCompleted: Boolean,
    val updatedAt: Double = FieldValue.serverTimestamp,
)