package com.github.droibit.firebase_todo.ui.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val isInDarkMode: Boolean
        get() {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return currentNightMode == Configuration.UI_MODE_NIGHT_YES
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.navHostContainer.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }

        val navHostFragment = binding.navHostContainer.getFragment<NavHostFragment>()
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener(this)
        binding.bottomNav.setupWithNavController(navController)

        if (isInDarkMode) {
            // workaround for removing the elevation color overlay
            // https://github.com/material-components/material-components-android/issues/1148
            binding.bottomNav.elevation = 0f
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        // TODO: show and hide bottom nav with a animation.
        binding.bottomNav.isVisible = when (destination.id) {
            R.id.newTaskFragment,
            R.id.updateTaskFragment,
            R.id.settingsFragment -> false
            else -> true
        }
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}
