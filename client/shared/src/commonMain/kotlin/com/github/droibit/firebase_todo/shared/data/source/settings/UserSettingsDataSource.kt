package com.github.droibit.firebase_todo.shared.data.source.settings

import co.touchlab.stately.freeze
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSettingsDataSource(
    private val settings: FlowSettings
) {
    val taskFilter: Flow<TaskFilter>
        get() {
            return settings.getIntFlow(
                key = Keys.taskFilter.name,
                defaultValue = Keys.taskFilter.defaultValue
            ).map { TaskFilter(it).freeze() }
        }

    suspend fun setTaskFilter(taskFilter: TaskFilter) {
        settings.putInt(Keys.taskFilter.name, taskFilter.id)
    }

    companion object Keys {
        internal val taskFilter = SettingsKey("taskFilter", TaskFilter.ALL.id)
    }
}