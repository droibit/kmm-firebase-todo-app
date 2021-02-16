package com.github.droibit.firebase_todo.ui.main

import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.imageLoader
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.utils.UserIconURL

fun Toolbar.setUserIcon(url: UserIconURL) {
    findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            val request = url.toRequest(context, iconSizeDp= 24)
            val result = context.imageLoader.execute(request)
            val accountMenuItem = menu.children.first { it.itemId == R.id.user }
            accountMenuItem.icon = checkNotNull(result.drawable)
        }
    }
}
