package com.github.droibit.firebase_todo.ui.main.task.list.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.droibit.firebase_todo.databinding.FragmentSortTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortTaskBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSortTaskBinding? = null
    private val binding get() = checkNotNull(_binding)

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
    }
}