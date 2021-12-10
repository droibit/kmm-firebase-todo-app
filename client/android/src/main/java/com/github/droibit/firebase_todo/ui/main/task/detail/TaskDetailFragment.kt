package com.github.droibit.firebase_todo.ui.main.task.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.databinding.FragmentTaskDetailBinding
import com.github.droibit.firebase_todo.shared.utils.consume
import com.github.droibit.firebase_todo.ui.main.task.detail.TaskDetailFragmentDirections.Companion.toUpdateTask
import com.github.droibit.firebase_todo.utils.navigateSafely
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class TaskDetailFragment :
    Fragment(),
    Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val args: TaskDetailFragmentArgs by navArgs()

    private val viewModel: TaskDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTaskDetailBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = viewLifecycleOwner
                it.viewModel = viewModel
                _binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {
            setOnMenuItemClickListener(this@TaskDetailFragment)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.fab.apply {
            setOnClickListener {
                findNavController()
                    .navigateSafely(toUpdateTask(task = args.task))
            }
            applyInsetter {
                type(navigationBars = true) {
                    margin()
                }
            }
        }

        binding.completedCheckBox.setOnClickListener {
            viewModel.toggleTaskCompletion()
        }

        viewModel.task.observe(viewLifecycleOwner) {
            binding.task = it
        }
        subscribeDeleteTaskUiModel()
        subscribeUpdateTaskCompletionUiModel()
    }

    private fun subscribeDeleteTaskUiModel() {
        viewModel.deleteTaskUiModel.observe(viewLifecycleOwner) { uiModel ->
            if (uiModel.inProgress) {
                TransitionManager.beginDelayedTransition(binding.container)
            }

            uiModel.error.consume()?.let { message ->
                Snackbar.make(binding.container, message.id, Snackbar.LENGTH_SHORT).show()
            }

            uiModel.success.consume()?.let {
                findNavController().popBackStack()
            }
        }
    }

    private fun subscribeUpdateTaskCompletionUiModel() {
        viewModel.updateTaskCompletionUiModel.observe(viewLifecycleOwner) { uiModel ->
            uiModel.error.consume()?.let { message ->
                Snackbar.make(binding.container, message.id, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // Toolbar.OnMenuItemClickListener

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_task -> {
                viewModel.deleteTask()
            }
        }
        return true
    }
}
