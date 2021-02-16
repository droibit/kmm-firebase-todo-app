package com.github.droibit.firebase_todo.ui.main.task.list.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.droibit.firebase_todo.databinding.FragmentSortTaskBinding
import com.github.droibit.firebase_todo.utils.setResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortTaskBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSortTaskBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val args: SortTaskBottomSheetDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSortTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sortingList.adapter = TaskSortingAdapter(args.currentTaskSorting) {
            val result = if (it.key == args.currentTaskSorting.key) {
                it.copy(order = it.order.toggled())
            } else {
                it
            }

            findNavController().run {
                previousBackStackEntry.setResult(RESULT_SELECTED_TASK_SORTING, result)
                popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val RESULT_SELECTED_TASK_SORTING = "RESULT_SELECTED_TASK_SORTING"
    }
}
