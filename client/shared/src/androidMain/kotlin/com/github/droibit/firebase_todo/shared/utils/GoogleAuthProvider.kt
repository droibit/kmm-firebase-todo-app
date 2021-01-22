package com.github.droibit.firebase_todo.shared.utils

import com.google.firebase.auth.GoogleAuthProvider as FirebaseGoogleAuthProvider
import dev.gitlive.firebase.auth.AuthCredential

internal actual object GoogleAuthProvider {
    actual fun credential(idToken: String, accessToken: String?): AuthCredential {
        return AuthCredential(
            FirebaseGoogleAuthProvider.getCredential(idToken, accessToken)
        )
    }
}