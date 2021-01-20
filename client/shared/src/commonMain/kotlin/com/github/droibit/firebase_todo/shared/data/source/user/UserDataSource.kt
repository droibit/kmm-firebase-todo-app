package com.github.droibit.firebase_todo.shared.data.source.user

import com.github.droibit.firebase_todo.shared.model.user.AuthException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import kotlin.coroutines.cancellation.CancellationException

class UserDataSource(
    private val auth: FirebaseAuth
) {
    val isSignedIn: Boolean get() = auth.currentUser != null

    @Throws(AuthException::class, CancellationException::class)
    suspend fun signInWithGoogle(idToken: String, accessToken: String): FirebaseUser {
        try {
            val credential = GoogleAuthProvider.credential(idToken, accessToken)
            val result = auth.signInWithCredential(credential)
            return requireNotNull(result.user)
        } catch (e: FirebaseAuthException) {
            throw AuthException(cause = e)
        }
    }
}