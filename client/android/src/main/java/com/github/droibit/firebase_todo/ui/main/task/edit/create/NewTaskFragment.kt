package com.github.droibit.firebase_todo.ui.main.task.edit.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.databinding.FragmentEditTaskBinding
import com.github.droibit.firebase_todo.utils.toggleSofInputVisibility
import dagger.hilt.android.AndroidEntryPoint

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
        toggleSofInputVisibility(visible = true)
    }

    override fun onPause() {
        super.onPause()

        toggleSofInputVisibility(visible = false)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
