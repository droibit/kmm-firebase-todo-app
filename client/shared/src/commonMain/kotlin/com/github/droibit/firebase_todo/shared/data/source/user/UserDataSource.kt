package com.github.droibit.firebase_todo.shared.data.source.user

import co.touchlab.stately.freeze
import com.github.droibit.firebase_todo.shared.model.user.AuthException
import com.github.droibit.firebase_todo.shared.model.user.User
import com.github.droibit.firebase_todo.shared.utils.GoogleAuthProvider
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import kotlin.coroutines.cancellation.CancellationException

class UserDataSource(
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
            return firebaseUser.toUser()
        } catch (e: FirebaseException) {
            throw AuthException(cause = e)
        }
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