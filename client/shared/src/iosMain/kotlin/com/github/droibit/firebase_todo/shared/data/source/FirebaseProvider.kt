package com.github.droibit.firebase_todo.shared.data.source

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

object FirebaseProvider {
    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = Firebase.firestore
}
