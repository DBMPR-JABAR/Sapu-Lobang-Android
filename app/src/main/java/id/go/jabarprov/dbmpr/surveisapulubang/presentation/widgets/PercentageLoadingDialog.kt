package id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutDialogProgressLoadingBinding

class PercentageLoadingDialog private constructor() : DialogFragment() {

    private lateinit var binding: LayoutDialogProgressLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDialogProgressLoadingBinding.inflate(inflater, container, false)
        return binding.root
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

    fun updateProgress(progress: Double) {
        if (isAdded) {
            binding.progressBar.progress = progress.toInt()
        }
    }

    companion object {
        private const val TAG = "Loading Dialog"

        fun create(): PercentageLoadingDialog {
            return PercentageLoadingDialog().apply { isCancelable = false }
        }
    }
}