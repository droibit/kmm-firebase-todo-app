package com.github.droibit.firebase_todo.ui.main.task.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.navigation.navArgs
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.ui.main.task.edit.new.NewTaskFragment
import dagger.hilt.android.AndroidEntryPoint

// TODO: Discontinue Activity and move to Navigation Component composed of Fragments.
@AndroidEntryPoint
class EditTaskActivity : AppCompatActivity(R.layout.activity_edit_task) {

    private val args: EditTaskActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                if (args.task == null) {
                    replace(R.id.fragmentContainer, NewTaskFragment.newInstance())
                }
            }
        }
    }
}