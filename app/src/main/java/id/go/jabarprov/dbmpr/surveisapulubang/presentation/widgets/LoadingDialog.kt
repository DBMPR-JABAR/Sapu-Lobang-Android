package id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import id.go.jabarprov.dbmpr.surveisapulubang.R

class LoadingDialog private constructor() : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_dialog_loading, container, false)
    }

    fun show(fragmentManager: FragmentManager) {
        val fragment = fragmentManager.findFragmentByTag(TAG)
        if (fragment == null) {
            super.show(fragmentManager, TAG)
        }
    }

    override fun dismiss() {
        if (isAdded) {
            super.dismiss()
        }
    }

    companion object {
        private const val TAG = "Loading Dialog"

        fun create(): LoadingDialog {
            return LoadingDialog().apply { isCancelable = false }
        }
    }
}