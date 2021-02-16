package com.github.droibit.firebase_todo.shared.utils

import dev.gitlive.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider as FirebaseGoogleAuthProvider

internal actual object GoogleAuthProvider {
    actual fun credential(idToken: String, accessToken: String?): AuthCredential {
        return AuthCredential(
            FirebaseGoogleAuthProvider.getCredential(idToken, accessToken)
        )
    }
}
