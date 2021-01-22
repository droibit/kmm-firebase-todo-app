package com.github.droibit.firebase_todo.ui.common

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["visibleUnless", "requestFocusOnVisible"], requireAll = false)
fun bindVisibleUnless(view: View, visible: Boolean, requestFocus: Boolean = false) {
    view.isVisible = visible

    if (view.isVisible && requestFocus) {
        view.requestFocus()
    }
}

@BindingAdapter(value = ["goneUnless", "requestFocusOnVisible"], requireAll = false)
fun bindGoneUnless(view: View, gone: Boolean, requestFocus: Boolean = false) {
    view.isGone = gone

    if (view.isVisible && requestFocus) {
        view.requestFocus()
    }
}
