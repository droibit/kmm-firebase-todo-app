package com.github.droibit.firebase_todo.ui.onboarding.entrypoint

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.droibit.firebase_todo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryPointFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            requireActivity().run {
                startActivity(MainActivity.createIntent(this))
                finish()
            }
        }
    }
}