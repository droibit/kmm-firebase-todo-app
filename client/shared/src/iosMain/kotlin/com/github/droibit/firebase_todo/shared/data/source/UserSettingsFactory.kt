package com.github.droibit.firebase_todo.shared.data.source

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings

object UserSettingsFactory {
    fun make(name: String?): FlowSettings {
        val settings: ObservableSettings = AppleSettings.Factory().create(name) as AppleSettings
        return settings.toFlowSettings()
    }
}