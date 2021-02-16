package com.github.droibit.firebase_todo.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.github.droibit.firebase_todo.R

class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_progress, this)

        context.withStyledAttributes(attrs, R.styleable.ProgressView, defStyleAttr) {
            val background = getColor(
                R.styleable.ProgressView_android_background,
                ContextCompat.getColor(context, R.color.black_800_alpha_010)
            )
            setBackgroundColor(background)
        }
        isFocusable = true
        isClickable = true
    }
}
