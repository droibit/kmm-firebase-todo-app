package com.github.droibit.firebase_todo.ui.main.settings.content

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.github.droibit.firebase_todo.R
import com.github.droibit.firebase_todo.utils.setResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SignOutConfirmationDialogFragment : DialogFragment(), DialogInterface.OnClickListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.settings_account_sign_out_dialog_message)
            .setPositiveButton(R.string.settings_account_sign_out_dialog_positive, this)
            .setNegativeButton(android.R.string.cancel, this)
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dispatchResult(confirmed = false)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dispatchResult(confirmed = which == DialogInterface.BUTTON_POSITIVE)
    }

    private fun dispatchResult(confirmed: Boolean) {
        findNavController().run {
            previousBackStackEntry.setResult(ARG_CONFIRMED_SIGN_OUT, confirmed)
            popBackStack()
        }
    }

    companion object {
        const val ARG_CONFIRMED_SIGN_OUT = "ARG_CONFIRMED_SIGN_OUT"
    }
}