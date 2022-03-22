package id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import coil.load
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutDialogDetailLubangBinding

class DetailLubangDialog private constructor() : DialogFragment() {

    private lateinit var binding: LayoutDialogDetailLubangBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDialogDetailLubangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonClose.setOnClickListener {
                dismiss()
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

    fun showImage(
        imageUrl: String,
        fragmentManager: FragmentManager,
        tag: String = "Detail Lubang"
    ) {
        binding.imageViewLubang.load(imageUrl)
        show(fragmentManager, tag)
    }

    companion object {
        fun create(): DetailLubangDialog {
            return DetailLubangDialog()
        }
    }
}