package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_penanganan

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutDialogKonfirmasiPenangananBinding
import id.go.jabarprov.dbmpr.surveisapulubang.utils.FILE_PROVIDER_AUTHORITY
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.createPictureCacheFile
import java.io.File
import kotlin.math.roundToInt

class ConfirmationPenangananDialog private constructor() : DialogFragment() {

    lateinit var positiveClickListener: (DialogFragment, String, File, Uri) -> Unit

    lateinit var negativeClickListener: (DialogFragment) -> Unit

    lateinit var binding: LayoutDialogKonfirmasiPenangananBinding

    private var photoUri: Uri? = null

    private var photoFile: File? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                dialog?.window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        500f,
                        resources.displayMetrics
                    )
                        .roundToInt()
                )
                binding.apply {
                    imageViewPenanganan.isVisible = true
                    imageViewPenanganan.setImageURI(photoUri)
                }
                checkButtonIsEnable()
            } else {
                photoFile = null
                photoUri = null
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                takePicture()
            } else {
                Toast.makeText(requireContext(), "Izin Kamera Ditolak", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private var keterangan: String? = null

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
            editTextPenanganan.setText(keterangan)

            buttonIya.setOnClickListener {
                positiveClickListener(
                    this@ConfirmationPenangananDialog,
                    editTextPenanganan.text.toString(),
                    photoFile!!,
                    photoUri!!
                )
            }

            buttonBatal.setOnClickListener {
                negativeClickListener(this@ConfirmationPenangananDialog)
            }

            editTextPenanganan.doOnTextChanged { text, _, _, _ ->
                keterangan = text.toString()
                checkButtonIsEnable()
            }

            buttonAmbilGambar.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                } else {
                    takePicture()
                }
            }
        }
    }

    private fun checkButtonIsEnable() {
        binding.buttonIya.apply {
            isEnabled = photoFile != null && photoUri != null && keterangan != null
        }
    }

    private fun takePicture() {
        photoFile = requireContext().createPictureCacheFile()
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            FILE_PROVIDER_AUTHORITY,
            photoFile!!
        )
        takePictureLauncher.launch(photoUri)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun setKeterangan(ket: String): ConfirmationPenangananDialog {
        keterangan = ket
        return this
    }

    private fun setOnPositiveClickListener(action: (dialog: DialogFragment, keteranganPenanganan: String, gambarPenanganan: File, uriGambar: Uri) -> Unit) {
        positiveClickListener = action
    }

    private fun setOnNegativeButtonClickListener(action: (dialog: DialogFragment) -> Unit) {
        negativeClickListener = action
    }

    companion object {
        fun create(
            onPositiveButtonClickListener: (dialog: DialogFragment, keteranganPenanganan: String, gambarPenanganan: File, uriGambar: Uri) -> Unit,
            onNegativeButtonClickListener: (dialog: DialogFragment) -> Unit
        ): ConfirmationPenangananDialog {
            return ConfirmationPenangananDialog().apply {
                setOnPositiveClickListener(onPositiveButtonClickListener)
                setOnNegativeButtonClickListener(onNegativeButtonClickListener)
            }
        }
    }
}