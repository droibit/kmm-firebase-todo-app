package com.github.droibit.firebase_todo.shared.data.source.settings

internal class SettingsKey<T>(val name: String, val defaultValue: T)

internal class OptionalSettingsKey<T>(val name: String, val defaultValue: T?)