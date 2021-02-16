package com.github.droibit.firebase_todo.ui.main.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.databinding.FragmentStatisticsBinding
import com.github.droibit.firebase_todo.ui.main.setUserIcon
import com.github.droibit.firebase_todo.ui.main.statistics.StatisticsFragmentDirections.Companion.toSettings
import com.github.droibit.firebase_todo.ui.main.task.MainViewModel
import com.github.droibit.firebase_todo.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment :
    Fragment(),
    Toolbar.OnMenuItemClickListener {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentStatisticsBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = this.viewLifecycleOwner
                this._binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnMenuItemClickListener(this)
        mainViewModel.userIconUrl.observe(viewLifecycleOwner) {
            binding.toolbar.setUserIcon(it)
        }

        // TODO: Remove behavior check code.
        binding.numOfActiveTasks.text = "100"
        binding.numOfCompletedTasks.text = "10"
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // Toolbar.OnMenuItemClickListener

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.user -> findNavController().navigateSafely(toSettings())
        }
        return true
    }
}
