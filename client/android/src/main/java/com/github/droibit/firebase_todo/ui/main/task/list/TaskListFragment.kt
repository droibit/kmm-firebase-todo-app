package com.github.droibit.firebase_todo.ui.main.task.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.github.aakira.napier.Napier
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.databinding.FragmentTaskListBinding
import com.github.droibit.firebase_todo.shared.model.task.Task
import com.github.droibit.firebase_todo.shared.model.task.TaskFilter
import com.github.droibit.firebase_todo.shared.model.task.TaskSorting
import com.github.droibit.firebase_todo.ui.main.setUserIcon
import com.github.droibit.firebase_todo.ui.main.task.MainViewModel
import com.github.droibit.firebase_todo.ui.main.task.list.TaskListFragmentDirections.Companion.toFilterTaskBottomSheet
import com.github.droibit.firebase_todo.ui.main.task.list.TaskListFragmentDirections.Companion.toNewTask
import com.github.droibit.firebase_todo.ui.main.task.list.TaskListFragmentDirections.Companion.toSettings
import com.github.droibit.firebase_todo.ui.main.task.list.TaskListFragmentDirections.Companion.toSortTaskBottomSheet
import com.github.droibit.firebase_todo.ui.main.task.list.TaskListFragmentDirections.Companion.toTaskDetail
import com.github.droibit.firebase_todo.ui.main.task.list.filter.FilterTaskBottomSheetDialogFragment.Companion.RESULT_SELECTED_TASK_FILTER
import com.github.droibit.firebase_todo.ui.main.task.list.sort.SortTaskBottomSheetDialogFragment.Companion.RESULT_SELECTED_TASK_SORTING
import com.github.droibit.firebase_todo.utils.consumeResult
import com.github.droibit.firebase_todo.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@AndroidEntryPoint
class TaskListFragment :
    Fragment(),
    TaskListHeaderView.OnClickListener,
    TaskListAdapter.ItemClickListener,
    Toolbar.OnMenuItemClickListener,
    LifecycleEventObserver {

    @Inject
    lateinit var listAdapter: TaskListAdapter

    private val taskListViewModel: TaskListViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

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
                it.lifecycleOwner = this.viewLifecycleOwner
                it.viewModel = this.taskListViewModel
                this._binding = it
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener(this)
        mainViewModel.userIconUrl.observe(viewLifecycleOwner) {
            binding.toolbar.setUserIcon(it)
        }

        binding.fab.setOnClickListener {
            findNavController().navigateSafely(toNewTask())
        }

        binding.taskList.apply {
            adapter = listAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        binding.taskListHeaderView.onClickListener = this

        taskListViewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->
            uiModel.success?.let {
                binding.taskListHeaderView.apply {
                    setTaskFilter(it.taskFilter)
                    setTaskSorting(it.taskSorting)
                }

                listAdapter.submitList(it.tasks) {
                    if (it.tasks.isNotEmpty()) {
                        val lm = binding.taskList.layoutManager as LinearLayoutManager
                        lm.scrollToPosition(0)
                    }
                }
            }

            uiModel.error?.let {
                // TODO:
            }
        }

        // ref. https://developer.android.com/guide/navigation/navigation-programmatic?hl=en
        currentBackStackEntry.lifecycle.addObserver(this)

        taskListViewModel.filterTaskNavigation.observe(viewLifecycleOwner) {
            it.consume()?.let { currentFilter ->
                findNavController().navigateSafely(toFilterTaskBottomSheet(currentFilter))
            }
        }

        taskListViewModel.sortTaskNavigation.observe(viewLifecycleOwner) {
            it.consume()?.let { currentSorting ->
                findNavController().navigateSafely(toSortTaskBottomSheet(currentSorting))
            }
        }
    }

    private fun beginDelayedTransition() {
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event != ON_RESUME) {
            return
        }

        currentBackStackEntry.consumeResult<TaskFilter>(
            RESULT_SELECTED_TASK_FILTER
        )?.let {
            Napier.d("Selected task filter: $it")
            taskListViewModel.onTaskFilterChanged(it)
        }

        currentBackStackEntry.consumeResult<TaskSorting>(
            RESULT_SELECTED_TASK_SORTING
        )?.let {
            Napier.d("Selected task sorting: $it")
            taskListViewModel.onTaskSortingChange(it)
        }
    }

    override fun onDestroyView() {
        _binding = null
        currentBackStackEntry.lifecycle.removeObserver(this)
        super.onDestroyView()
    }

    // - TaskListHeaderView.OnClickListener

    override fun onFilterTaskClick() {
        taskListViewModel.onFilterTaskClick()
    }

    override fun onChangeSortKeyClick() {
        taskListViewModel.onChangeSortKeyClick()
    }

    // Toolbar.OnMenuItemClickListener

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.user -> findNavController().navigateSafely(toSettings())
        }
        return true
    }

    // TaskListAdapter.ItemClickListener

    override fun onItemTaskClick(task: Task) {
        findNavController()
            .navigateSafely(toTaskDetail(task = task))
    }

    override fun onTaskCheckboxClick(task: Task) {
        // TODO: Not yet implemented.
    }
}
