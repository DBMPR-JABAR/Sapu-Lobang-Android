package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_penanganan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutDialogKonfirmasiPenangananBinding

class ConfirmationPenangananDialog private constructor() : DialogFragment() {

    lateinit var positiveClickListener: (DialogFragment, String) -> Unit

    lateinit var negativeClickListener: (DialogFragment) -> Unit

    lateinit var binding: LayoutDialogKonfirmasiPenangananBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDialogKonfirmasiPenangananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            buttonIya.setOnClickListener {
                positiveClickListener(
                    this@ConfirmationPenangananDialog,
                    editTextPenanganan.text.toString()
                )
            }
            buttonBatal.setOnClickListener {
                negativeClickListener(this@ConfirmationPenangananDialog)
            }
            editTextPenanganan.doOnTextChanged { text, _, _, _ ->
                buttonIya.isEnabled = !text.isNullOrBlank()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setOnPositiveClickListener(action: (dialog: DialogFragment, keteranganPenanganan: String) -> Unit) {
        positiveClickListener = action
    }

    private fun setOnNegativeButtonClickListener(action: (dialog: DialogFragment) -> Unit) {
        negativeClickListener = action
    }

    companion object {
        fun create(
            onPositiveButtonClickListener: (dialog: DialogFragment, keteranganPenanganan: String) -> Unit,
            onNegativeButtonClickListener: (dialog: DialogFragment) -> Unit
        ): ConfirmationPenangananDialog {
            return ConfirmationPenangananDialog().apply {
                setOnPositiveClickListener(onPositiveButtonClickListener)
                setOnNegativeButtonClickListener(onNegativeButtonClickListener)
            }
        }
    }
}