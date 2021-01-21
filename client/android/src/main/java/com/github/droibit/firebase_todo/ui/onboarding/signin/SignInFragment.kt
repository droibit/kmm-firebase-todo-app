package com.github.droibit.firebase_todo.ui.onboarding.signin

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import com.github.aakira.napier.Napier
import com.github.droibit.firebase_todo.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val signInWithGoogle = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode != Activity.RESULT_OK || it.data == null) {
            return@registerForActivityResult
        }

        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            // Google Sign In was successful, authenticate with Firebase
            val account = requireNotNull(task.getResult(ApiException::class.java))
            Napier.d("firebaseAuthWithGoogle: ${account.idToken}")
        } catch (e: ApiException) {
            Napier.e("Google sign in failed", e)
        }
    }

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSignInBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = viewLifecycleOwner
                this._binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            signInWithGoogle.launch(googleSignInClient.signInIntent)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}