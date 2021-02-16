package com.github.droibit.firebase_todo.ui.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.droibit.firebase_todo.shared.data.repository.user.UserRepository
import com.github.droibit.firebase_todo.utils.UserIconURL
import com.github.droibit.firebase_todo.utils.getIconURL
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val userPhotoUrlSink = MutableLiveData(
        checkNotNull(userRepository.currentUser).getIconURL()
    )
    val userIconUrl: LiveData<UserIconURL> get() = userPhotoUrlSink
}
