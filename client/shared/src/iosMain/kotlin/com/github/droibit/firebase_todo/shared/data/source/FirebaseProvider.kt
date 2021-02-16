package com.github.droibit.firebase_todo.shared.data.source

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth

object FirebaseAuthProvider {
    val auth: FirebaseAuth = Firebase.auth
}
