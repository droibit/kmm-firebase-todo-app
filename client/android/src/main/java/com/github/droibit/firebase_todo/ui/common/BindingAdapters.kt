package com.github.droibit.firebase_todo.ui.common

import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

@BindingAdapter("android:checked")
fun CheckableImageView.bindChecked(checked: Boolean) {
    isChecked = checked
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

@BindingAdapter("textTaskCreatedAt")
fun TextView.bindTaskCreatedAt(previousCreatedAtMillis: Long, createdAtMillis: Long) {
    if (previousCreatedAtMillis == createdAtMillis) {
        return
    }

    text = DateUtils.formatDateTime(
        context,
        createdAtMillis,
        DateUtils.FORMAT_SHOW_YEAR or
            DateUtils.FORMAT_SHOW_DATE or
            DateUtils.FORMAT_SHOW_WEEKDAY or
            DateUtils.FORMAT_ABBREV_ALL or
            DateUtils.FORMAT_SHOW_TIME
    )
}
