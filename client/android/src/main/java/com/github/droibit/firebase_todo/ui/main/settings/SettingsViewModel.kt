package com.github.droibit.firebase_todo.ui.main.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.droibit.firebase_todo.shared.data.repository.user.UserRepository
import com.github.droibit.firebase_todo.shared.model.app.AppVersion
import com.github.droibit.firebase_todo.ui.main.settings.content.SettingsContentUiModel
import com.github.droibit.firebase_todo.ui.main.settings.content.SignOutUiModel
import com.github.droibit.firebase_todo.utils.getIconURL
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@HiltViewModel
class SettingsViewModel(
    private val userRepository: UserRepository,
    appVersion: AppVersion,
    private val sinOutUiModelSink: MutableLiveData<SignOutUiModel>
) : ViewModel() {

    val contentUiModel: LiveData<SettingsContentUiModel> by lazy(NONE) {
        val user = checkNotNull(userRepository.currentUser)
        MutableLiveData(
            SettingsContentUiModel(
                userName = user.name ?: "---",
                userIconURL = user.getIconURL(),
                appVersion = appVersion
            )
        )
    }

    val sinOutUiModel: LiveData<SignOutUiModel> get() = sinOutUiModelSink

    @Inject
    constructor(
        userRepository: UserRepository,
        appVersion: AppVersion
    ) : this(
        userRepository,
        appVersion,
        MutableLiveData()
    )
}