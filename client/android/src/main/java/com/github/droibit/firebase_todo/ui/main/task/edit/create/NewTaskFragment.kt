package com.github.droibit.firebase_todo.ui.main.task.edit.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.databinding.FragmentEditTaskBinding
import com.github.droibit.firebase_todo.shared.utils.consume
import com.github.droibit.firebase_todo.utils.hideSofInputIfIsRemoving
import com.github.droibit.firebase_todo.utils.toggleSofInputVisibility
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

@AndroidEntryPoint
class NewTaskFragment : Fragment() {
    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: NewTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentEditTaskBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = viewLifecycleOwner
                it.viewModel = viewModel
                _binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setTitle(R.string.new_task_title)
        binding.fab.apply {
            setOnClickListener {
                toggleSofInputVisibility(visible = false)
                viewModel.createTask()
            }
            applyInsetter {
                type(navigationBars = true) {
                    margin()
                }
            }
        }
        toggleSofInputVisibility(visible = true)

        subscribeCreateTaskUiModel()
    }

    private fun subscribeCreateTaskUiModel() {
        viewModel.createTaskUiModel.observe(viewLifecycleOwner) { uiModel ->
            if (uiModel.inProgress) {
                TransitionManager.beginDelayedTransition(binding.container)
            }

            uiModel.error.consume()?.let { message ->
                Snackbar.make(binding.container, message.id, Snackbar.LENGTH_SHORT).show()
            }

            uiModel.success.consume()?.let { message ->
                Toast.makeText(requireContext(), message.id, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        hideSofInputIfIsRemoving()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
