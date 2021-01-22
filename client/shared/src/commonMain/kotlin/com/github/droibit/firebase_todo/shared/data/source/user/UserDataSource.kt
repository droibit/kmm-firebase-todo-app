package com.github.droibit.firebase_todo.shared.data.source.user

import com.github.droibit.firebase_todo.shared.model.user.AuthException
import com.github.droibit.firebase_todo.shared.model.user.User
import com.github.droibit.firebase_todo.shared.utils.GoogleAuthProvider
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseUser
import kotlin.coroutines.cancellation.CancellationException

class UserDataSource(
    private val auth: FirebaseAuth
) {
    val isSignedIn: Boolean get() = auth.currentUser != null

    @Throws(AuthException::class, CancellationException::class)
    suspend fun signInWithGoogle(idToken: String, accessToken: String?): User {
        try {
            val credential = GoogleAuthProvider.credential(idToken, accessToken)
            val result = auth.signInWithCredential(credential)
            val firebaseUser = requireNotNull(result.user)
            return User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName,
                email = firebaseUser.email,
                photoURL = firebaseUser.photoURL
            )
        } catch (e: FirebaseException) {
            throw AuthException(cause = e)
        }
    }
}