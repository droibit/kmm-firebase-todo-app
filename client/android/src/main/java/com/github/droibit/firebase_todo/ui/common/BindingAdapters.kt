package com.github.droibit.firebase_todo.ui.common

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.github.droibit.firebase_todo.R

@BindingAdapter(
    "visibleUnless",
    "requestFocusOnVisible",
    requireAll = false
)
fun View.bindVisibleUnless(visible: Boolean, requestFocus: Boolean = false) {
    isVisible = visible

    if (isVisible && requestFocus) {
        requestFocus()
    }
}

@BindingAdapter(
    "goneUnless",
    "requestFocusOnVisible",
    requireAll = false
)
fun View.bindGoneUnless(gone: Boolean, requestFocus: Boolean = false) {
    isGone = gone

    if (isVisible && requestFocus) {
        requestFocus()
    }
}

@BindingAdapter("marginBottomNavInsets")
fun View.bindMarginBottomNavInsets(
    previousApplyBottom: Boolean,
    applyBottom: Boolean
) {
    if (previousApplyBottom == applyBottom) {
        return
    }

    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        this.bottomMargin = if (applyBottom) {
            context.resources.getDimensionPixelSize(R.dimen.bottom_navigation_height)
        } else {
            0
        }
    }
}

@BindingAdapter("enabledPopBackNav")
fun Toolbar.bindEnabledPopBackNavigation(previousEnabled: Boolean, enabled: Boolean) {
    if (previousEnabled == enabled) {
        return
    }

    if (enabled) {
        setNavigationIcon(R.drawable.ic_round_arrow_back)
        setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    } else {
        navigationIcon = null
        setNavigationOnClickListener(null)
    }
}
