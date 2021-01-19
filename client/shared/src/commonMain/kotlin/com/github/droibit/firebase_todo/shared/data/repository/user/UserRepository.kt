package com.github.droibit.firebase_todo.shared.data.repository.user

import com.github.droibit.firebase_todo.shared.data.source.user.UserDataSource
import com.github.droibit.firebase_todo.shared.utils.CoroutinesDispatcherProvider
import kotlinx.coroutines.withContext

class UserRepository(
    private val dataSource: UserDataSource,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {

    val isSignedIn: Boolean get() = dataSource.isSignedIn

    suspend fun signInWithGoogle(idToken: String, accessToken: String) {
        withContext(dispatcherProvider.io) {
            val user = dataSource.signInWithGoogle(idToken, accessToken)
        }
    }
}