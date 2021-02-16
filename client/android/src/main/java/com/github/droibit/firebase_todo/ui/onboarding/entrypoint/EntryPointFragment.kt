package com.github.droibit.firebase_todo.ui.onboarding.entrypoint

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.droibit.firebase_todo.ui.main.MainActivity
import com.github.droibit.firebase_todo.ui.onboarding.entrypoint.EntryPointFragmentDirections.Companion.toSignIn
import com.github.droibit.firebase_todo.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryPointFragment : Fragment() {

    private val viewModel: EntryPointViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (viewModel.launchDestination) {
            LaunchDestination.MAIN -> navigateToMain()
            LaunchDestination.SIGN_IN -> navigateToSignIn()
        }
    }

    private fun navigateToMain() {
        requireActivity().run {
            startActivity(MainActivity.createIntent(this))
            finish()
        }
    }

    private fun navigateToSignIn() {
        lifecycleScope.launchWhenStarted {
            findNavController().navigateSafely(toSignIn())
        }
    }
}
