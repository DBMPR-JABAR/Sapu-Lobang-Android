package id.go.jabarprov.dbmpr.surveisapulubang.presentation.preview_photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentPreviewPhotoBinding
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog

class PreviewPhotoFragment : Fragment() {

    private val args: PreviewPhotoFragmentArgs by navArgs()

    private lateinit var binding: FragmentPreviewPhotoBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            if (args.imageUrl != null) {
                imageViewLubang.load(args.imageUrl) {
                    listener(
                        onStart = {
                            loadingDialog.show(childFragmentManager)
                        },
                        onSuccess = { _, _ ->
                            loadingDialog.dismiss()
                        },
                        onError = { _, _ ->
                            loadingDialog.dismiss()
                        },
                    )
                }
            }

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}