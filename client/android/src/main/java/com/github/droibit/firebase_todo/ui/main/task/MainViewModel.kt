package com.github.droibit.firebase_todo.ui.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.droibit.firebase_todo.BuildConfig
import com.github.droibit.firebase_todo.shared.data.repository.user.UserRepository
import com.github.droibit.firebase_todo.shared.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val userPhotoUrlSink = MutableLiveData(
        checkNotNull(userRepository.currentUser).getPhotoURL()
    )
    val userPhotoUrl: LiveData<String> get() = userPhotoUrlSink
}

private fun User.getPhotoURL(packageName: String = BuildConfig.APPLICATION_ID): String {
    return photoURL ?: "android.resource://$packageName/drawable/ic_round_person"
}