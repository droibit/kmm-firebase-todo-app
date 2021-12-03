package com.github.droibit.firebase_todo.ui.main.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.droibit.firebase_todo.shared.data.repository.task.TaskRepository
import com.github.droibit.firebase_todo.shared.model.task.Statistics
import com.github.droibit.firebase_todo.ui.common.MessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@HiltViewModel
class StatisticsViewModel(
    private val taskRepository: TaskRepository,
    private val getStatisticsUiModelSink: MutableLiveData<GetStatisticsUiModel>
) : ViewModel() {
    val uiModel: LiveData<GetStatisticsUiModel> by lazy(NONE) {
        taskRepository.statistics
            .onStart { emitUiModel(inProgress = true) }
            .onEach { emitUiModel(success = it) }
            .catch {
                // TODO: Error handling.
            }
            .launchIn(viewModelScope)
        getStatisticsUiModelSink
    }

    @Inject
    constructor(
        taskRepository: TaskRepository
    ) : this(
        taskRepository,
        getStatisticsUiModelSink = MutableLiveData()
    )

    init {
        emitUiModel()
    }

    private fun emitUiModel(
        inProgress: Boolean = false,
        success: Statistics? = null,
        error: MessageUiModel? = null
    ) {
        getStatisticsUiModelSink.value =
            GetStatisticsUiModel(inProgress, success, error)
    }
}
