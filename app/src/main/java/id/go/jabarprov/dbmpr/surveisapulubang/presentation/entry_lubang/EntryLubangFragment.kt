package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_lubang

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryLubangBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.KategoriLubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.SurveiLubangViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store.SurveiLubangAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.FILE_PROVIDER_AUTHORITY
import id.go.jabarprov.dbmpr.surveisapulubang.utils.LocationUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.createPictureCacheFile
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.getValueOrElse
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "EntryLubangFragment"

@AndroidEntryPoint
class EntryLubangFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    private val surveiLubangViewModel: SurveiLubangViewModel by viewModels()

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION,
                false
            ) -> {
                setUpLocationSetting()
            }
            permissions.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                false
            ) -> {
                setUpLocationSetting()
            }
            else -> {
                Toast.makeText(requireContext(), "Izin Lokasi Ditolak", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) surveiLubangViewModel.processAction(
                SurveiLubangAction.UpdateFotoLubang(
                    photoUri,
                    photoFile
                )
            )
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

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
    }

    private val locationUtils by lazy { LocationUtils(requireActivity()) }

    private val loadingDialog by lazy { LoadingDialog.create() }

    lateinit var binding: FragmentEntryLubangBinding

    private val timePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    surveiLubangViewModel.processAction(SurveiLubangAction.UpdateTanggal(it))
                }
                addOnNegativeButtonClickListener {
                    dismiss()
                }
            }
    }

    private lateinit var photoUri: Uri

    private lateinit var photoFile: File

    private fun setUpLocationSetting() {
        locationUtils.enableLocationService()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
            )
        } else {
            setUpLocationSetting()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryLubangBinding.inflate(inflater, container, false)
        observeAuthState()
        observeSurveiLubangState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        checkLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun initUI() {
        setVisibilityFormEntry(false)

        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonPilihTanggal.setOnClickListener {
                timePicker.show(childFragmentManager, "Date Picker Dialog")
            }

            editTextRuasJalan.doOnTextChanged { text, _, _, _ ->
                surveiLubangViewModel.processAction(SurveiLubangAction.UpdateRuasJalan(text.toString()))
            }

            editTextKodeLokasi.doOnTextChanged { text, _, _, _ ->
                surveiLubangViewModel.processAction(SurveiLubangAction.UpdateKodeLokasi(text.toString()))
            }

            editTextLokasiKm.doOnTextChanged { text, _, _, _ ->
                surveiLubangViewModel.processAction(SurveiLubangAction.UpdateLokasiKm(text.toString()))
            }

            editTextLokasiM.doOnTextChanged { text, _, _, _ ->
                surveiLubangViewModel.processAction(SurveiLubangAction.UpdateLokasiM(text.toString()))
            }

            editTextPanjangLubang.doOnTextChanged { text, _, _, _ ->
                surveiLubangViewModel.processAction(
                    SurveiLubangAction.UpdatePanjangLubang(
                        text.toString().toDoubleOrNull().getValueOrElse(0.0)
                    )
                )
            }

            editTextJumlahLubangGroup.doOnTextChanged { text, _, _, _ ->
                surveiLubangViewModel.processAction(
                    SurveiLubangAction.UpdateJumlahLubangPerGroup(
                        text.toString().toIntOrNull().getValueOrElse(0)
                    )
                )
            }

            radioGroupKategori.setOnCheckedChangeListener { radioGroup, checkedId ->
                if (checkedId == R.id.radio_button_kategori_single) {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.UpdateKategoriLubang(
                            KategoriLubang.SINGLE
                        )
                    )
                } else {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.UpdateKategoriLubang(
                            KategoriLubang.GROUP
                        )
                    )
                }
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

            buttonStart.setOnClickListener {
                surveiLubangViewModel.processAction(
                    SurveiLubangAction.StartSurveiAction(
                        surveiLubangViewModel.uiState.value.tanggal,
                        surveiLubangViewModel.uiState.value.idRuasJalan
                    )
                )
            }

            buttonTambahLubangSingle.setOnClickListener {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.TambahLubangAction(
                            it.latitude,
                            it.longitude,
                        )
                    )
                }
            }

            buttonKurangLubangSingle.setOnClickListener {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.KurangLubangAction(
                            it.latitude,
                            it.longitude,
                        )
                    )
                }
            }

            buttonTambahLubangGroup.setOnClickListener {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.TambahLubangAction(
                            it.latitude,
                            it.longitude,
                        )
                    )
                }
            }
        }
    }

    private fun takePicture() {
        photoFile = requireContext().createPictureCacheFile()
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            FILE_PROVIDER_AUTHORITY,
            photoFile
        )
        takePictureLauncher.launch(photoUri)
    }

    private fun setVisibilityFormEntry(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.apply {
            buttonStart.visibility = if (isVisible) View.GONE else View.VISIBLE
            textViewLabelKategori.visibility = visibility
            radioGroupKategori.visibility = visibility
            textViewLabelLokasi.visibility = visibility
            textFieldKodeLokasi.visibility = visibility
            textViewLabelKm.visibility = visibility
            textFieldLokasiKm.visibility = visibility
            textViewSymbolTambah.visibility = visibility
            textFieldLokasiM.visibility = visibility
            textViewLabelPanjangLubang.visibility = visibility
            constraintLayoutContainerPanjangLubang.visibility = visibility
            textViewLabelUploadFoto.visibility = visibility
            constraintLayoutContainerUploadFoto.visibility = visibility
            textViewLabelTambahLubang.visibility = visibility
//            buttonLihatHasilSurvei.visibility = visibility

            if (isVisible) {
                setVisibilityKategoriLubang(surveiLubangViewModel.uiState.value.kategoriLubang)
            } else {
                textViewJumlahLubangSingle.visibility = visibility
                textViewLabelLubangSingle.visibility = visibility
                buttonTambahLubangSingle.visibility = visibility
                buttonKurangLubangSingle.visibility = visibility

                editTextJumlahLubangGroup.visibility = visibility
                buttonTambahLubangGroup.visibility = visibility
            }
        }
    }

    private fun setVisibilityKategoriLubang(kategoriLubang: KategoriLubang) {
        val singleLubangVisibility =
            if (kategoriLubang == KategoriLubang.SINGLE) View.VISIBLE else View.GONE
        val groupLubangVisibility =
            if (kategoriLubang == KategoriLubang.GROUP) View.VISIBLE else View.GONE
        binding.apply {
            textViewJumlahLubangSingle.visibility = singleLubangVisibility
            textViewLabelLubangSingle.visibility = singleLubangVisibility
            buttonTambahLubangSingle.visibility = singleLubangVisibility
            buttonKurangLubangSingle.visibility = singleLubangVisibility

            editTextJumlahLubangGroup.visibility = groupLubangVisibility
            buttonTambahLubangGroup.visibility = groupLubangVisibility
        }
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.uiState.collect {
                    if (it.user != null) {
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            it.user.ruasJalan.map { ruasJalan -> "${ruasJalan.namaRuas} - ${ruasJalan.id}" })
                        binding.editTextRuasJalan.setAdapter(adapter)
                    }
                }
            }
        }
    }

    private fun observeSurveiLubangState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                surveiLubangViewModel.uiState.collect {
                    if (it.isLoading) {
                        loadingDialog.show(childFragmentManager, "Loading Dialog")
                    }

                    if (it.isSuccess) {
                        loadingDialog.dismiss()
                    }

                    if (it.isFailed) {
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        loadingDialog.dismiss()
                    }

                    setVisibilityFormEntry(it.isStarted)

                    binding.apply {
                        buttonStart.isEnabled = it.idRuasJalan != ""
                        textViewContentTanggal.text =
                            CalendarUtils.formatCalendarToString(it.tanggal)

                        textFieldRuasJalan.isEnabled = !it.isStarted
                        buttonPilihTanggal.isVisible = !it.isStarted
                        buttonStart.isVisible = !it.isStarted

                        textViewJumlahLubangSingle.text = it.jumlahLubang.toString()

                        imageViewLubang.setImageURI(it.gambarLubangUri)
                        imageViewLubang.isVisible = it.gambarLubangUri != null

                        /**
                         * Button Enable If
                         * kode lokasi filled
                         * lokasi km filled
                         * lokasi m filled
                         * panjang lubang filled
                         * gambar filled
                         * */

                        buttonTambahLubangSingle.isEnabled =
                            it.isStarted && it.kodeLokasi.isNotBlank() && it.lokasiKm.isNotBlank() && it.lokasiM.isNotBlank() && it.panjangLubang > 0 && it.gambarLubangUri != null

                        buttonTambahLubangGroup.isEnabled =
                            it.isStarted && it.kodeLokasi.isNotBlank() && it.lokasiKm.isNotBlank() && it.lokasiM.isNotBlank() && it.panjangLubang > 0 && it.gambarLubangUri != null && it.jumlahLubangPerGroup > 0

                        buttonKurangLubangSingle.isEnabled =
                            it.isStarted && it.kodeLokasi.isNotBlank() && it.lokasiKm.isNotBlank() && it.lokasiM.isNotBlank() && it.panjangLubang > 0 && it.gambarLubangUri != null && it.jumlahLubang > 0
                    }

                }
            }
        }
    }
}