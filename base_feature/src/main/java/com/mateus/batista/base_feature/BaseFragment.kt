package com.mateus.batista.base_feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mateus.batista.base_feature.listeners.OnItemDialogErrorClickListener
import com.mateus.batista.base_feature.util.ErrorDialogFragment
import com.mateus.batista.base_feature.util.ErrorDialogFragment.Companion.DESCRIPTION_DIALOG
import com.mateus.batista.base_feature.util.ErrorDialogFragment.Companion.TITLE_DIALOG
import com.mateus.batista.base_feature.util.showDialog
import kotlinx.android.synthetic.*

open class BaseFragment: Fragment(), OnItemDialogErrorClickListener {
    val navController by lazy { findNavController() }
    private lateinit var onClickConfirmation: () -> Unit

    fun handleErrors(throwable: Throwable, onClickConfirmation: () -> Unit) {
        showGenericError(onClickConfirmation, throwable.message ?: "")
    }

    private fun showGenericError(onClickConfirmation: () -> Unit, error: String){
        showErrorDialog(getString(R.string.error_title), getString(R.string.error_description, error)) {
            onClickConfirmation()
        }
    }

    private fun showErrorDialog(title: String, description: String, onClickConfirmation: () -> Unit) {
        val arg = Bundle()
        arg.putString(TITLE_DIALOG, title)
        arg.putString(DESCRIPTION_DIALOG, description)
        showDialog(ErrorDialogFragment(), arg)
        this.onClickConfirmation = onClickConfirmation
    }

    override fun onItemDialogClick() {
        this.onClickConfirmation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }
}