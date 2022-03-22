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

class PreviewPhotoFragment : Fragment() {

    private val args: PreviewPhotoFragmentArgs by navArgs()

    private lateinit var binding: FragmentPreviewPhotoBinding

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
                imageViewLubang.load(args.imageUrl)
            }

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}