package com.github.droibit.firebase_todo.shared.data.repository.user

import com.github.aakira.napier.Napier
import com.github.droibit.firebase_todo.shared.data.source.user.UserDataSource
import com.github.droibit.firebase_todo.shared.model.user.AuthException
import com.github.droibit.firebase_todo.shared.model.user.User
import com.github.droibit.firebase_todo.shared.utils.CoroutinesDispatcherProvider
import kotlin.coroutines.cancellation.CancellationException

class UserRepository(
    private val dataSource: UserDataSource,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {
    val isSignedIn: Boolean get() = dataSource.isSignedIn

    val currentUser: User? get() = dataSource.currentUser

    @Throws(AuthException::class, CancellationException::class)
    suspend fun signInWithGoogle(idToken: String, accessToken: String?): User {
        // Avoid thread switching to avoid IncorrectDereferenceException on iOS.
        return dataSource.signInWithGoogle(idToken, accessToken)
            .also {
                Napier.d("Signed in user: name:${it.name}, email:${it.email}, photoURL: ${it.photoURL}")
            }
    }
}