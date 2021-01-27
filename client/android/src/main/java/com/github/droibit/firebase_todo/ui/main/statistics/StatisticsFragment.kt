package com.github.droibit.firebase_todo.ui.main.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.droibit.firebase_todo.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentStatisticsBinding.inflate(inflater, container, false)
            .also {
                it.lifecycleOwner = this.viewLifecycleOwner
                this._binding = it
            }.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}