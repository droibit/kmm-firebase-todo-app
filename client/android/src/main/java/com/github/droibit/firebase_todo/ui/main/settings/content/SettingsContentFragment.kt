package com.github.droibit.firebase_todo.ui.main.settings.content

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import coil.imageLoader
import com.github.aakira.napier.Napier
import com.github.droibit.chopstick.preference.bindPreference
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.shared.utils.consume
import com.github.droibit.firebase_todo.ui.main.settings.SettingsFragmentDirections.Companion.toOnBoarding
import com.github.droibit.firebase_todo.ui.main.settings.SettingsFragmentDirections.Companion.toSignOutConfirmation
import com.github.droibit.firebase_todo.ui.main.settings.SettingsViewModel
import com.github.droibit.firebase_todo.ui.main.settings.content.SignOutConfirmationDialogFragment.Companion.ARG_CONFIRMED_SIGN_OUT
import com.github.droibit.firebase_todo.utils.UserIconURL
import com.github.droibit.firebase_todo.utils.consumeResult
import com.github.droibit.firebase_todo.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlin.LazyThreadSafetyMode.NONE

@AndroidEntryPoint
class SettingsContentFragment : PreferenceFragmentCompat(),
    LifecycleEventObserver {

    private val viewModel: SettingsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private val profilePref: Preference by bindPreference(R.string.settings_account_profile_key)

    private val signOutPref: Preference by bindPreference(R.string.settings_account_sign_out_key)

    private val appVersionPref: Preference by bindPreference(R.string.settings_app_build_version_key)

    private val currentBackStackEntry: NavBackStackEntry by lazy(NONE) {
        findNavController().getBackStackEntry(R.id.settingsFragment)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOutPref.setOnPreferenceClickListener {
            findNavController().navigateSafely(toSignOutConfirmation())
            true
        }

        // ref. https://developer.android.com/guide/navigation/navigation-programmatic?hl=en
        currentBackStackEntry.lifecycle.addObserver(this)

        viewModel.contentUiModel.observe(viewLifecycleOwner) {
            profilePref.title = it.userName
            showUserIcon(it.userIconURL)
            appVersionPref.summary = it.getAppVersion(requireContext())
        }

        viewModel.signOutUiModel.observe(viewLifecycleOwner) { uiModel ->
            uiModel.success.consume()?.let {
                findNavController().navigateSafely(toOnBoarding())
                requireActivity().finish()
            }
        }
    }

    private fun showUserIcon(url: UserIconURL) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val context = requireContext()
            val request = url.toRequest(context, iconSizeDp = 36)
            val result = context.imageLoader.execute(request)
            profilePref.icon = checkNotNull(result.drawable)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event != Lifecycle.Event.ON_RESUME) {
            return
        }

        currentBackStackEntry.consumeResult<Boolean>(
            ARG_CONFIRMED_SIGN_OUT
        )?.let {
            Napier.d("Sign out confirmed: $it")
            viewModel.onSignOutConfirmed(confirmed = it)
        }
    }
}