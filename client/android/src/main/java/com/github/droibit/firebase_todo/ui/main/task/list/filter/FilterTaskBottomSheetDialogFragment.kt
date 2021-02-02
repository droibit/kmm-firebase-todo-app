package com.github.droibit.firebase_todo.ui.main.task.list.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.droibit.firebase_todo.databinding.FragmentFilterTaskBinding
import com.github.droibit.firebase_todo.ui.main.task.list.filter.FilterTaskBottomSheetDialogFragmentArgs
import com.github.droibit.firebase_todo.utils.setResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterTaskBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterTaskBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val args: FilterTaskBottomSheetDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filterList.adapter = FilterTaskAdapter(args.currentTaskFilter) {
            with(findNavController()) {
                previousBackStackEntry.setResult(RESULT_SELECTED_TASK_FILTER, it)
                popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val RESULT_SELECTED_TASK_FILTER = "RESULT_SELECTED_TASK_FILTER"
    }
}