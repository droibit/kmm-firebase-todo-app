package com.github.droibit.firebase_todo.utils

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import io.github.aakira.napier.Napier

fun Fragment.toggleSofInputVisibility(visible: Boolean, contentView: View = checkNotNull(view)) {
    val focusedView = contentView.allViews.firstOrNull { it.isFocused } ?: return
    if (visible) {
        focusedView.toggleSofInputVisibility(visible = true)
    } else {
        focusedView.toggleSofInputVisibility(visible = false)
    }
}

fun Fragment.hideSofInputIfIsRemoving(contentView: View = checkNotNull(view)) {
    val focusedView = contentView.allViews.firstOrNull { it.isFocused } ?: return
    if (isRemoving) {
        focusedView.toggleSofInputVisibility(visible = false)
    }
}

fun View.toggleSofInputVisibility(visible: Boolean) {
    if (!isFocused) {
        Napier.w("View does not have focus.")
    }

    val imm = checkNotNull(ContextCompat.getSystemService(context, InputMethodManager::class.java))
    if (visible) {
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    } else {
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
