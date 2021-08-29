package com.nyan.pokenyan.ui.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

open class BaseDialogFragment : DialogFragment() {
    override fun show(manager: FragmentManager, tag: String?) {
        if (!isShowing) super.show(manager, tag)
        isShowing = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isShowing = false
    }

    companion object {
        private var isShowing = false
    }
}
