@file:Suppress("RUNTIME_ANNOTATION_NOT_SUPPORTED")

package com.github.droibit.firebase_todo.shared.data.source.user

import co.touchlab.stately.freeze
import com.chrynan.inject.Inject
import com.chrynan.inject.Singleton
import com.github.droibit.firebase_todo.shared.model.user.AuthException
import com.github.droibit.firebase_todo.shared.model.user.User
import com.github.droibit.firebase_todo.shared.utils.GoogleAuthProvider
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import io.github.aakira.napier.Napier
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class UserDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {
    val currentUser: User?
        get() {
            return auth.currentUser?.toUser()
        }

    val isSignedIn: Boolean get() = auth.currentUser != null

    @Throws(AuthException::class, CancellationException::class)
    suspend fun signInWithGoogle(idToken: String, accessToken: String?): User {
        try {
            val credential = GoogleAuthProvider.credential(idToken, accessToken)
                .apply { freeze() }
            val result = auth.signInWithCredential(credential)
            val firebaseUser = requireNotNull(result.user)
            Napier.d("User's provider data: ${firebaseUser.providerData.map { it.providerId }}")
            return firebaseUser.toUser()
        } catch (e: FirebaseException) {
            throw AuthException(cause = e)
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }
}

private fun FirebaseUser.toUser(): User {
    return User(
        uid = this.uid,
        name = this.displayName,
        email = this.email,
        photoURL = this.photoURL
    )
}
