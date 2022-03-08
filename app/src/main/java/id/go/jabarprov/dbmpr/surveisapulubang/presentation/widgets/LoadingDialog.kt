package id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.go.jabarprov.dbmpr.surveisapulubang.R

class LoadingDialog private constructor() : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_dialog_loading, container, false)
    }

    companion object {
        fun create(): LoadingDialog {
            return LoadingDialog().apply { isCancelable = false }
        }
    }
}