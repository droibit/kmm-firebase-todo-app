package com.github.droibit.firebase_todo.ui.onboarding.signin

import android.content.Intent
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.aakira.napier.Napier
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.data.repository.user.UserRepository
import com.github.droibit.firebase_todo.shared.model.user.AuthException
import com.github.droibit.firebase_todo.shared.utils.Event
import com.github.droibit.firebase_todo.ui.common.MessageUiModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel(
    private val userRepository: UserRepository,
    private val googleSignInClient: GoogleSignInClient,
    private val uiModelSink: MutableLiveData<SignInUiModel>,
    private val signInWithGoogleSink: MutableLiveData<Event<Intent>>
) : ViewModel() {

    val uiModel: LiveData<SignInUiModel> get() = uiModelSink

    val signInWithGoogle: LiveData<Event<Intent>> get() = signInWithGoogleSink

    @Inject
    constructor(
        userRepository: UserRepository,
        googleSignInClient: GoogleSignInClient
    ) : this(
        userRepository,
        googleSignInClient,
        MutableLiveData(),
        MutableLiveData()
    )

    init {
        emitUiModel()
    }

    @MainThread
    fun signInWithGoogle() {
        emitUiModel(inProgress = true)
        signInWithGoogleSink.value = Event(googleSignInClient.signInIntent)
    }

    @MainThread
    fun onSignInWithGoogleResult(getSignedInAccountTask: Task<GoogleSignInAccount>) {
        try {
            val account = checkNotNull(getSignedInAccountTask.getResult(ApiException::class.java))
            signInWithGoogle(checkNotNull(account.idToken))
        } catch (e: ApiException) {
            Napier.e("Google sign in failed", e)
            emitUiModel(
                inProgress = false,
                error = Event(MessageUiModel(R.string.sign_in_failed))
            )
        }
    }

    private fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                userRepository.signInWithGoogle(idToken, accessToken = null)
                emitUiModel(
                    inProgress = false,
                    success = Event(Unit)
                )
            } catch (e: AuthException) {
                Napier.e("Authentication failed", e)

                emitUiModel(
                    inProgress = false,
                    error = Event(MessageUiModel(R.string.sign_in_failed))
                )
            }
        }
    }

    private fun emitUiModel(
        inProgress: Boolean = false,
        success: Event<Unit>? = null,
        error: Event<MessageUiModel>? = null,
    ) {
        uiModelSink.value = SignInUiModel(inProgress, success, error)
    }
}