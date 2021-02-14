package com.github.droibit.firebase_todo.ui.main.settings.content

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import coil.imageLoader
import com.github.droibit.chopstick.preference.bindPreference
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.ui.main.settings.SettingsViewModel
import com.github.droibit.firebase_todo.utils.UserIconURL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsContentFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private val profilePref: Preference by bindPreference(R.string.settings_account_profile_key)

    private val appVersionPref: Preference by bindPreference(R.string.settings_app_build_version_key)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.contentUiModel.observe(viewLifecycleOwner) {
            profilePref.title = it.userName
            showUserIcon(it.userIconURL)

            appVersionPref.summary = it.getAppVersion(requireContext())
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
}