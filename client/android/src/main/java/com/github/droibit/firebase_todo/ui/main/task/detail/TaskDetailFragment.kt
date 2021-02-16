package com.github.droibit.firebase_todo.ui.main.task.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.databinding.FragmentTaskDetailBinding
import com.github.droibit.firebase_todo.ui.main.task.detail.TaskDetailFragmentDirections.Companion.toUpdateTask
import com.github.droibit.firebase_todo.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDetailFragment :
    Fragment(),
    Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val args: TaskDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTaskDetailBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = viewLifecycleOwner
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

        binding.fab.setOnClickListener {
            findNavController()
                .navigateSafely(toUpdateTask(task = args.task))
        }

        binding.task = args.task
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // Toolbar.OnMenuItemClickListener

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_task -> {
                // TODO: Remove behavior check code.
                findNavController().popBackStack()
            }
        }
        return true
    }
}
