package com.github.droibit.firebase_todo.utils

import android.content.Context
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.github.droibit.firebase_todo.BuildConfig
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.model.user.User

fun User.getIconURL(packageName: String = BuildConfig.APPLICATION_ID): UserIconURL {
    val url = photoURL ?: "android.resource://$packageName/drawable/ic_round_person"
    return UserIconURL(url)
}

inline class UserIconURL(val url: String) {
    fun toRequest(context: Context, iconSizeDp: Int): ImageRequest {
        val iconSizePx = (iconSizeDp * context.resources.displayMetrics.density).toInt()
        return ImageRequest.Builder(context)
            .data(url)
            .error(R.drawable.ic_round_person)
            .size(iconSizePx)
            .transformations(CircleCropTransformation())
            .build()
    }
}