package com.github.droibit.firebase_todo.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import io.github.aakira.napier.Napier

fun NavController.navigateSafely(directions: NavDirections) {
    if (currentDestination?.getAction(directions.actionId) == null) {
        Napier.w("Action corresponding to Directions($directions) could not be found.")
        return
    }
    navigate(directions)
}
