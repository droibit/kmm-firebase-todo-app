package com.github.droibit.firebase_todo.ui.main.task.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.aakira.napier.Napier
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.databinding.FragmentTaskListBinding
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.ui.main.task.list.TaskListFragmentDirections.Companion.toFilterTaskBottomSheet
import com.github.droibit.firebase_todo.ui.main.task.list.TaskListFragmentDirections.Companion.toNewTask
import com.github.droibit.firebase_todo.ui.main.task.list.TaskListFragmentDirections.Companion.toSortTaskBottomSheet
import com.github.droibit.firebase_todo.ui.main.task.list.filter.FilterTaskBottomSheetDialogFragment.Companion.RESULT_SELECTED_TASK_FILTER
import com.github.droibit.firebase_todo.ui.main.task.list.sort.SortTaskBottomSheetDialogFragment.Companion.RESULT_SELECTED_TASK_SORTING
import com.github.droibit.firebase_todo.utils.consumeResult
import com.github.droibit.firebase_todo.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@AndroidEntryPoint
class TaskListFragment : Fragment(),
    Toolbar.OnMenuItemClickListener,
    TaskListHeaderView.OnClickListener,
    LifecycleEventObserver {

    @Inject
    lateinit var listAdapter: TaskListAdapter

    private val viewModel: TaskListViewModel by viewModels()

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val currentBackStackEntry: NavBackStackEntry by lazy(NONE) {
        findNavController().getBackStackEntry(R.id.taskListFragment)
    }

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
            List(30) {
                Task(
                    id = "id-$it",
                    title = "Title-$it",
                    description = if (it % 3 == 0) "Description-$it" else "",
                    isCompleted = it % 2 == 0,
                    createdAt = System.currentTimeMillis()
                )
            }
        )
        binding.fab.setOnClickListener {
            findNavController().navigateSafely(toNewTask())
        }
        binding.taskListHeaderView.onClickListener = this

        // ref. https://developer.android.com/guide/navigation/navigation-programmatic?hl=en
        currentBackStackEntry.lifecycle.addObserver(this)

        viewModel.filterTaskNavigation.observe(viewLifecycleOwner) {
            it.consume()?.let { currentFilter ->
                findNavController().navigateSafely(toFilterTaskBottomSheet(currentFilter))
            }
        }

        viewModel.sortTaskNavigation.observe(viewLifecycleOwner) {
            it.consume()?.let { currentSorting ->
                findNavController().navigateSafely(toSortTaskBottomSheet(currentSorting))
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event != ON_RESUME) {
            return
        }

        currentBackStackEntry.consumeResult<TaskFilter>(
            RESULT_SELECTED_TASK_FILTER
        )?.let {
            Napier.d("Selected task filter: $it")
        }

        currentBackStackEntry.consumeResult<TaskSorting>(
            RESULT_SELECTED_TASK_SORTING
        )?.let {
            Napier.d("Selected task sorting: $it")
        }
    }

    override fun onDestroyView() {
        _binding = null
        currentBackStackEntry.lifecycle.removeObserver(this)
        super.onDestroyView()
    }

    // - Toolbar.OnMenuItemClickListener

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return true
    }

    // - TaskListHeaderView.OnClickListener

    override fun onFilterTaskClick() {
        // viewModel.onFilterTaskClick()

        // TODO: Must Remove
        findNavController().navigateSafely(toFilterTaskBottomSheet(TaskFilter.DEFAULT))
    }

    override fun onChangeSortKeyClick() {
        // viewModel.onChangeSortKeyClick()

        // TODO: Must Remove
        findNavController().navigateSafely(toSortTaskBottomSheet(TaskSorting.DEFAULT))
    }
}