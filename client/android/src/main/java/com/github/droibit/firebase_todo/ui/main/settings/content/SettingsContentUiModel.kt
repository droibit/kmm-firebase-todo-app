package com.github.droibit.firebase_todo.ui.main.settings.content

import android.content.Context
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.model.app.AppVersion
import com.github.droibit.firebase_todo.utils.UserIconURL

data class SettingsContentUiModel(
    val userName: String,
    val userIconURL: UserIconURL,
    private val appVersion: AppVersion
) {
    fun getAppVersion(context: Context): String {
        return context.getString(
            R.string.settings_app_build_version_summary,
            appVersion.name,
            appVersion.code
        )
    }
}