package com.github.droibit.firebase_todo.shared.data.source.settings

import co.touchlab.stately.freeze
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
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

    val taskSorting: Flow<TaskSorting>
        get() {
            return settings.getStringFlow(
                key = Keys.taskSorting.name,
                defaultValue = Keys.taskSorting.defaultValue.toCSV()
            ).map {
                val csv = it.split(",")
                TaskSorting(
                    key = TaskSorting.Key(csv[0].toInt()),
                    order = TaskSorting.Order(csv[1].toInt())
                )
            }
        }

    suspend fun setTaskFilter(taskFilter: TaskFilter) {
        settings.putInt(Keys.taskFilter.name, taskFilter.id)
    }

    suspend fun setTaskSorting(taskSorting: TaskSorting) {
        val text = "${taskSorting.key},${taskSorting.order}"
        settings.putString(Keys.taskSorting.name, taskSorting.toCSV())
    }

    internal companion object Keys {
        val taskFilter = SettingsKey("taskFilter", TaskFilter.DEFAULT.id)
        val taskSorting = SettingsKey("taskSorting", TaskSorting.default)
    }
}

private fun TaskSorting.toCSV(): String {
    return "$key,$order"
}