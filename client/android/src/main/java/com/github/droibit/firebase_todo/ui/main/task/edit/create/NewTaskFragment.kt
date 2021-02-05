package com.github.droibit.firebase_todo.ui.main.task.edit.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.droibit.firebase_todo.databinding.FragmentNewTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : Fragment() {
    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: NewTaskViewModel by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun hideKeyboard() {
        listOf<View>(
            binding.titleEditText,
            binding.descriptionEditText
        ).forEach {
            if (it.isFocused) {
                val imm =
                    ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
                checkNotNull(imm).hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    companion object {
        fun newInstance() = NewTaskFragment()
    }
}
