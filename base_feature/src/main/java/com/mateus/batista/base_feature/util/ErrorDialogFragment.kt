package com.mateus.batista.base_feature.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.mateus.batista.base_feature.R
import com.mateus.batista.base_feature.listeners.OnItemDialogErrorClickListener

class ErrorDialogFragment : DialogFragment() {

    companion object {
        const val TITLE_DIALOG = "titleDialog"
        const val DESCRIPTION_DIALOG = "descriptionDialog"
    }
    var ok: LinearLayout? = null
    var cancel: LinearLayout? = null
    var title: TextView? = null
    var description: TextView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.dialog_error, null)

        ok = view?.findViewById(R.id.ok)
        cancel = view?.findViewById(R.id.cancel)
        title = view?.findViewById(R.id.title)
        description = view?.findViewById(R.id.descriptionText)

        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setView(view)

        title?.text = arguments?.getString(TITLE_DIALOG)
        description?.text = arguments?.getString(DESCRIPTION_DIALOG)

        ok?.setOnClickListener {
            if (targetFragment != null ) {
                (targetFragment as (OnItemDialogErrorClickListener))
                    .onItemDialogClick()
            }else{
                (activity as (OnItemDialogErrorClickListener))
                    .onItemDialogClick()
            }
            dismiss()
        }
        cancel?.setOnClickListener { dismiss() }
        return alertDialog.create()
    }
}

