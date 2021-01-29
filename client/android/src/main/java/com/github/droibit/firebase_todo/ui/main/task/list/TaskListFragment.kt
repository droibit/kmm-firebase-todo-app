package com.github.droibit.firebase_todo.ui.main.task.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.droibit.firebase_todo.databinding.FragmentTaskListBinding
import com.github.droibit.firebase_todo.shared.model.task.Task
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskListFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = checkNotNull(_binding)

    @Inject
    lateinit var listAdapter: TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTaskListBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = viewLifecycleOwner
                this._binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener(this)
        binding.taskList.apply {
            adapter = listAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        listAdapter.submitList(
            List(30) { Task(
                id ="id-$it",
                title = "Title-$it",
                description = if (it % 3 == 0) "Description-$it" else "",
                isCompleted = it % 2 == 0
            ) }
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return true
    }
}