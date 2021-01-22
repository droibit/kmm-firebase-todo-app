package com.github.droibit.firebase_todo.shared.utils

import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.GoogleAuthProvider

internal actual object GoogleAuthProvider {
    actual fun credential(
        idToken: String,
        accessToken: String?
    ): AuthCredential {
        return GoogleAuthProvider.credential(idToken, requireNotNull(accessToken))
    }
}