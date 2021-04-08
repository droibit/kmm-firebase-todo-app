package com.github.droibit.firebase_todo.ui.main.statistics

import com.github.droibit.firebase_todo.shared.model.task.Statistics
import com.github.droibit.firebase_todo.ui.common.MessageUiModel

data class GetStatisticsUiModel(
    val inProgress: Boolean,
    val success: Statistics?,
    val error: MessageUiModel?
)