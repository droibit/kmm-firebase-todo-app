package com.github.droibit.firebase_todo.ui.main

import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.github.droibit.firebase_todo.R

fun Toolbar.setUserIcon(url: String) {
    findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            val iconSizePx = (24 * context.resources.displayMetrics.density).toInt()
            val request = ImageRequest.Builder(context)
                .data(url)
                .error(R.drawable.ic_round_person)
                .size(iconSizePx)
                .transformations(CircleCropTransformation())
                .build()
            val result = context.imageLoader.execute(request)
            val accountMenuItem = menu.children.first { it.itemId == R.id.user }
            accountMenuItem.icon = checkNotNull(result.drawable)
        }
    }
}
