package com.github.droibit.firebase_todo.ui.main.task.list

import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object TaskListFragmentModule {

    @Provides
    fun provideItemClickListener(fragment: Fragment): TaskListAdapter.ItemClickListener {
        return fragment as TaskListAdapter.ItemClickListener
    }
}