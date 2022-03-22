package id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutDialogKonfirmasiBinding

class KonfirmasiDialog private constructor() : DialogFragment() {

    private lateinit var binding: LayoutDialogKonfirmasiBinding
    private var titleDialog = ""
    private var descriptionDialog = ""
    private var onPositiveButtonClickListener: ((DialogFragment) -> Unit)? = null
    private var onNegativeButtonClickListener: ((DialogFragment) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDialogKonfirmasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textViewTitle.text = titleDialog
            textViewDescription.text = descriptionDialog
            buttonIya.setOnClickListener {
                onPositiveButtonClickListener?.invoke(this@KonfirmasiDialog)
            }
            buttonBatal.setOnClickListener {
                onNegativeButtonClickListener?.invoke(this@KonfirmasiDialog)
            }
        }
    }

    fun setTitle(title: String): KonfirmasiDialog {
        titleDialog = title
        return this
    }

    fun setDescription(description: String): KonfirmasiDialog {
        descriptionDialog = description
        return this
    }

    fun setOnPositiveButtonClickListener(action: (DialogFragment) -> Unit): KonfirmasiDialog {
        onPositiveButtonClickListener = action
        return this
    }

    fun setOnNegativeButtonClickListener(action: (DialogFragment) -> Unit): KonfirmasiDialog {
        onNegativeButtonClickListener = action
        return this
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        fun create(
            title: String,
            description: String,
            onPositive: (DialogFragment) -> Unit,
            onNegative: (DialogFragment) -> Unit
        ): KonfirmasiDialog {
            return KonfirmasiDialog().apply {
                setTitle(title)
                setDescription(description)
                setOnPositiveButtonClickListener(onPositive)
                setOnNegativeButtonClickListener(onNegative)
            }
        }
    }
}