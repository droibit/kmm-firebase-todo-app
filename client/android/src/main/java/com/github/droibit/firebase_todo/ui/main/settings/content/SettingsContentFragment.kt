package com.github.droibit.firebase_todo.ui.main.settings.content

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.github.droibit.firebase_todo.R

class SettingsContentFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }
}