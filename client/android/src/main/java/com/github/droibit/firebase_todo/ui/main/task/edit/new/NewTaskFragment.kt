package com.github.droibit.firebase_todo.ui.main.task.edit.new

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.core.view.updateLayoutParams
import com.github.droibit.firebase_todo.databinding.FragmentNewTaskBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewTaskFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentNewTaskBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = viewLifecycleOwner
                _binding = it
            }.root
    }

    @Suppress("LocalVariableName")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val _dialog = super.onCreateDialog(savedInstanceState)
        _dialog.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val parentLayout = checkNotNull(
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            )

            val behaviour = BottomSheetBehavior.from(parentLayout)
            // Set up full screen
            parentLayout.updateLayoutParams {
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            parentLayout.doOnNextLayout {
                behaviour.peekHeight = it.height
            }
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return _dialog
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}