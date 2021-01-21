package com.github.droibit.firebase_todo.ui.onboarding.entrypoint

import androidx.lifecycle.ViewModel
import com.github.droibit.firebase_todo.shared.data.repository.user.UserRepository
import com.github.droibit.firebase_todo.ui.onboarding.entrypoint.LaunchDestination.MAIN
import com.github.droibit.firebase_todo.ui.onboarding.entrypoint.LaunchDestination.SIGN_IN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EntryPointViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val launchDestination: LaunchDestination
        get() = if (userRepository.isSignedIn) MAIN else SIGN_IN
}