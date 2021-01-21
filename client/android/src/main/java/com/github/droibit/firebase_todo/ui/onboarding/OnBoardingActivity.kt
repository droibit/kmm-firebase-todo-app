package com.github.droibit.firebase_todo.ui.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.droibit.firebase_todo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
    }
}