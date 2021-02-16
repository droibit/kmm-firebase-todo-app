package com.github.droibit.firebase_todo.ui.main.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.droibit.firebase_todo.shared.data.repository.user.UserRepository
import com.github.droibit.firebase_todo.shared.model.app.AppVersion
import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel
import com.github.droibit.firebase_todo.ui.main.settings.content.SettingsContentUiModel
import com.github.droibit.firebase_todo.ui.main.settings.content.SignOutUiModel
import com.github.droibit.firebase_todo.utils.getIconURL
import com.github.droibit.firebase_todo.utils.requireValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@HiltViewModel
class SettingsViewModel(
    private val userRepository: UserRepository,
    appVersion: AppVersion,
    private val signOutUiModelSink: MutableLiveData<SignOutUiModel>
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

    val signOutUiModel: LiveData<SignOutUiModel> get() = signOutUiModelSink

    @Inject
    constructor(
        userRepository: UserRepository,
        appVersion: AppVersion
    ) : this(
        userRepository,
        appVersion,
        signOutUiModelSink = MutableLiveData()
    )

    init {
        emitSignOutUiModel()
    }

    fun onSignOutConfirmed(confirmed: Boolean) {
        if (confirmed) {
            signOut()
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            if (signOutUiModelSink.requireValue().inProgress) {
                return@launch
            }

            emitSignOutUiModel(inProgress = true)

            userRepository.signOut()

            emitSignOutUiModel(
                inProgress = false,
                success = Event(Unit)
            )
        }
    }

    private fun emitSignOutUiModel(
        inProgress: Boolean = false,
        success: Event<Unit>? = null,
        error: Event<MessageUiModel>? = null,
    ) {
        signOutUiModelSink.value = SignOutUiModel(inProgress, success, error)
    }
}