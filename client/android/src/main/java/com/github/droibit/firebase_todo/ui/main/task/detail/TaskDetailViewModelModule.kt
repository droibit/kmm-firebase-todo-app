package com.github.droibit.firebase_todo.ui.main.task.detail

import androidx.lifecycle.SavedStateHandle
import com.github.droibit.firebase_todo.shared.model.task.Task
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object TaskDetailViewModelModule {
    @Named("detail")
    @Provides
    fun provideTask(savedStateHandle: SavedStateHandle): Task {
        return requireNotNull(savedStateHandle.get("task"))
    }
}
