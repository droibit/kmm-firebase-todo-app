package com.github.droibit.firebase_todo.shared.utils

import dev.gitlive.firebase.auth.AuthCredential

// Workaround for passing null to accessToken.
internal expect object GoogleAuthProvider {
    fun credential(idToken: String, accessToken: String?): AuthCredential
}