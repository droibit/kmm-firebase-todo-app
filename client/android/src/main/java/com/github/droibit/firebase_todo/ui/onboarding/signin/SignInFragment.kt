package com.github.droibit.firebase_todo.ui.onboarding.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.TransitionManager
import com.github.droibit.firebase_todo.databinding.FragmentSignInBinding
import com.github.droibit.firebase_todo.shared.utils.consume
import com.github.droibit.firebase_todo.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()

    private val signInWithGoogle = registerForActivityResult(StartActivityForResult()) {
        val resultTask = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        viewModel.onSignInWithGoogleResult(resultTask)
    }

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSignInBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = this.viewLifecycleOwner
                it.viewModel = this.viewModel
                this._binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            viewModel.signInWithGoogle()
        }

        viewModel.signInWithGoogle.observe(viewLifecycleOwner) { event ->
            event.consume()?.let {
                signInWithGoogle.launch(it)
            }
        }

        // ref. https://github.com/google-admin/plaid/blob/main/designernews/src/main/java/io/plaidapp/designernews/ui/login/LoginActivity.kt
        viewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->
            if (uiModel.inProgress) {
                beginDelayedTransition()
            }

            uiModel.error.consume()?.let {
                showSignInError(messageId = it.id)
            }

            uiModel.success.consume()?.let {
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        requireActivity().run {
            val intent = MainActivity.createIntent(this)
            startActivity(intent)
            finish()
        }
    }

    private fun showSignInError(@StringRes messageId: Int) {
        Snackbar.make(binding.container, messageId, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun beginDelayedTransition() {
        TransitionManager.beginDelayedTransition(binding.container)
    }
}