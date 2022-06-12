package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_lubang

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.esri.arcgisruntime.data.QueryParameters
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.geometry.Envelope
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.ogc.wfs.WfsFeatureTable
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleRenderer
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryLubangBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.*
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.SurveiLubangViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store.SurveiLubangAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.PercentageLoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.FILE_PROVIDER_AUTHORITY
import id.go.jabarprov.dbmpr.surveisapulubang.utils.LocationUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.createPictureCacheFile
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.getValueOrElse
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
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
                initLocation()
            }
            permissions.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                false
            ) -> {
                setUpLocationSetting()
                initLocation()
            }
            else -> {
                Toast.makeText(requireContext(), "Izin Lokasi Ditolak", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess && photoFile != null && photoUri != null) {
                surveiLubangViewModel.processAction(
                    SurveiLubangAction.UpdateFotoLubang(
                        photoUri!!,
                        photoFile!!
                    )
                )
            } else {
                showErrorTakePictureToast()
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

    private val locationUtils by lazy { LocationUtils(requireActivity()) }

    private val percentageLoadingDialog by lazy { PercentageLoadingDialog.create() }

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

    private var photoUri: Uri? = null

    private var photoFile: File? = null

    private fun setUpLocationSetting() {
        if (!locationUtils.isLocationEnabled()) {
            lifecycleScope.launchWhenResumed {
                locationUtils.enableLocationService()
            }
        }
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
            initLocation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryLubangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        checkLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun initUI() {
        setVisibilityFormEntry(false)
        setUpArcGISMap()
        loadRuasJalan()
        observeAuthState()
        observeSurveiLubangState()

        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonPilihTanggal.setOnClickListener {
                timePicker.show(childFragmentManager, "Date Picker Dialog")
            }

            buttonCekLokasi.setOnClickListener {
                findNavController().navigate(EntryLubangFragmentDirections.actionEntryLubangFragmentToMapFragment())
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

            radioGroupKategori.setOnCheckedChangeListener { _, checkedId ->
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

            radioGroupLajur.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radio_button_kiri -> {
                        surveiLubangViewModel.processAction(
                            SurveiLubangAction.UpdateLajur(
                                Lajur.KIRI
                            )
                        )
                    }
                    R.id.radio_button_kanan -> {
                        surveiLubangViewModel.processAction(
                            SurveiLubangAction.UpdateLajur(
                                Lajur.KANAN
                            )
                        )
                    }
                    else -> {
                        surveiLubangViewModel.processAction(
                            SurveiLubangAction.UpdateLajur(
                                Lajur.AS
                            )
                        )
                    }
                }
            }

            radioGroupUkuranLubang.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == R.id.radio_button_kecil) {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.UpdateUkuran(
                            Ukuran.KECIL
                        )
                    )
                } else {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.UpdateUkuran(
                            Ukuran.BESAR
                        )
                    )
                }
            }

            radioGroupKedalamanLubang.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == R.id.radio_button_dangkal) {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.UpdateKedalaman(
                            Kedalaman.DANGKAL
                        )
                    )
                } else {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.UpdateKedalaman(
                            Kedalaman.DALAM
                        )
                    )
                }
            }

            radioGroupPotensiLubang.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == R.id.radio_button_potensi_true) {
                    surveiLubangViewModel.processAction(SurveiLubangAction.UpdatePotensiLubang(true))
                } else {
                    surveiLubangViewModel.processAction(SurveiLubangAction.UpdatePotensiLubang(false))
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

            editTextKeterangan.doOnTextChanged { text, _, _, _ ->
                surveiLubangViewModel.processAction(SurveiLubangAction.UpdateKeterangan(text.toString()))
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
                lifecycleScope.launchWhenResumed {
                    surveiLubangViewModel.processAction(SurveiLubangAction.GetLocation)
                    val location = locationUtils.getCurrentLocation(CancellationTokenSource().token)
                    if (location == null) {
                        surveiLubangViewModel.processAction(SurveiLubangAction.GetLocationFailed("Gagal Mengambil Lokasi"))
                    } else {
                        surveiLubangViewModel.processAction(
                            SurveiLubangAction.TambahLubangAction(
                                location.latitude,
                                location.longitude,
                            ) {
                                percentageLoadingDialog.updateProgress(it)
                            }
                        )
                    }
                }
            }

            buttonKurangLubangSingle.setOnClickListener {
                lifecycleScope.launchWhenResumed {
                    surveiLubangViewModel.processAction(SurveiLubangAction.GetLocation)
                    val location = locationUtils.getCurrentLocation(CancellationTokenSource().token)
                    if (location == null) {
                        surveiLubangViewModel.processAction(SurveiLubangAction.GetLocationFailed("Gagal Mengambil Lokasi"))
                    } else {
                        surveiLubangViewModel.processAction(
                            SurveiLubangAction.KurangLubangAction(
                                location.latitude,
                                location.longitude,
                            )
                        )
                    }
                }
            }

            buttonTambahLubangGroup.setOnClickListener {
                lifecycleScope.launchWhenResumed {
                    surveiLubangViewModel.processAction(SurveiLubangAction.GetLocation)
                    val location = locationUtils.getCurrentLocation(CancellationTokenSource().token)
                    if (location == null) {
                        surveiLubangViewModel.processAction(SurveiLubangAction.GetLocationFailed("Gagal Mengambil Lokasi"))
                    } else {
                        surveiLubangViewModel.processAction(
                            SurveiLubangAction.TambahLubangAction(
                                location.latitude,
                                location.longitude,
                            ) {
                                percentageLoadingDialog.updateProgress(it)
                            }
                        )
                    }
                }
            }

            buttonLihatHasilSurvei.setOnClickListener {
                findNavController().navigate(
                    EntryLubangFragmentDirections.actionEntryLubangFragmentToDetailSurveiFragment(
                        CalendarUtils.formatCalendarToString(surveiLubangViewModel.uiState.value.tanggal),
                        surveiLubangViewModel.uiState.value.idRuasJalan
                    )
                )
            }
        }
    }

    private fun setUpArcGISMap() {
        binding.mapViewArcgis.apply {
            map = ArcGISMap(BasemapStyle.ARCGIS_NAVIGATION)
            interactionOptions = MapView.InteractionOptions(this).apply {
                isPanEnabled = false
                isRotateEnabled = false
                isZoomEnabled = false
            }
            setViewpoint(
                Viewpoint(-6.921359549350742, 107.61111502699526, 72000.0)
            )
        }
    }

    private fun loadRuasJalan() {
        // Simple renderer for override style in feature layer
        val lineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.rgb(0, 0, 255), 5f)
        val simpleRenderer = SimpleRenderer(lineSymbol)

        val wfsFeatureTable = WfsFeatureTable(
            "https://geo.temanjabar.net/geoserver/wfs?service=wfs&version=2.0.0&request=GetCapabilities",
            "temanjabar:0_rj_prov_v",
        )

        wfsFeatureTable.featureRequestMode = ServiceFeatureTable.FeatureRequestMode.MANUAL_CACHE

        val featureLayer = FeatureLayer(wfsFeatureTable)
        featureLayer.renderer = simpleRenderer

        binding.mapViewArcgis.map.operationalLayers.add(featureLayer)

        binding.mapViewArcgis.addNavigationChangedListener {
            if (!it.isNavigating) {
                popoulateFromServer(wfsFeatureTable, binding.mapViewArcgis.visibleArea.extent)
            }
        }
    }

    private fun initLocation() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (!locationUtils.isLocationEnabled()) {
                    locationUtils.enableLocationService()
                }
                binding.mapViewArcgis.locationDisplay.autoPanMode =
                    LocationDisplay.AutoPanMode.RECENTER
                binding.mapViewArcgis.locationDisplay.startAsync()
            }
        }
    }

    private fun popoulateFromServer(wfsFeatureTable: WfsFeatureTable, extent: Envelope) {
        val visibleExtentQuery = QueryParameters()
        visibleExtentQuery.geometry = extent
        wfsFeatureTable.populateFromServiceAsync(visibleExtentQuery, false, null)
    }

    override fun onResume() {
        super.onResume()
        if (surveiLubangViewModel.uiState.value.idRuasJalan.isNotBlank()) {
            surveiLubangViewModel.processAction(
                SurveiLubangAction.StartSurveiAction(
                    surveiLubangViewModel.uiState.value.tanggal,
                    surveiLubangViewModel.uiState.value.idRuasJalan
                )
            )
        }
        binding.mapViewArcgis.resume()
    }

    override fun onPause() {
        binding.mapViewArcgis.pause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.mapViewArcgis.dispose()
        super.onDestroy()

    }

    private fun takePicture() {
        photoFile = requireContext().createPictureCacheFile()
        if (photoFile == null) {
            return showErrorTakePictureToast()
        }
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            FILE_PROVIDER_AUTHORITY,
            photoFile!!
        )
        if (photoUri == null) {
            return showErrorTakePictureToast()
        }
        takePictureLauncher.launch(photoUri)
    }

    private fun showErrorTakePictureToast() {
        showToast("Gagal mengambil gambar")
    }

    private fun clearInputLubang() {
        binding.apply {
            editTextKeterangan.text = null
            editTextPanjangLubang.text = null
            editTextJumlahLubangGroup.text = null
            radioGroupLajur.clearCheck()
            radioGroupUkuranLubang.clearCheck()
            radioGroupKedalamanLubang.clearCheck()
            radioGroupPotensiLubang.check(R.id.radio_button_potensi_false)
        }
    }

    private fun setVisibilityFormEntry(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.apply {
            buttonStart.visibility = if (isVisible) View.GONE else View.VISIBLE
            buttonCekLokasi.visibility = visibility
            mapViewArcgis.visibility = visibility
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
            textViewLabelKeterangan.visibility = visibility
            textViewLabelTotalPanjang.visibility = visibility
            constraintLayoutContainerTotalPanjangLubang.visibility = visibility
            textFieldKeterangan.visibility = visibility
            textViewLabelTambahLubang.visibility = visibility
            buttonLihatHasilSurvei.visibility = visibility
            textViewLabelLajur.visibility = visibility
            radioGroupLajur.visibility = visibility
            textViewLabelUkuranLubang.visibility = visibility
            radioGroupUkuranLubang.visibility = visibility
            textViewNoteDiameterLubang.visibility = visibility
            textViewLabelKedalamanLubang.visibility = visibility
            radioGroupKedalamanLubang.visibility = visibility
            textViewNoteKedalamanLubang.visibility = visibility
            textViewLabelPotensiLubang.visibility = visibility
            radioGroupPotensiLubang.visibility = visibility
            textViewNotePotensiLubang.visibility = visibility

            if (isVisible) {
                setVisibilityKategoriLubang(surveiLubangViewModel.uiState.value.kategoriLubang)
            } else {
                textViewJumlahLubangSingle.visibility = visibility
                textViewLabelLubangSingle.visibility = visibility
                buttonTambahLubangSingle.visibility = visibility
//                buttonKurangLubangSingle.visibility = visibility

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
//            buttonKurangLubangSingle.visibility = singleLubangVisibility

            editTextJumlahLubangGroup.visibility = groupLubangVisibility
            buttonTambahLubangGroup.visibility = groupLubangVisibility
        }
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.uiState.collect {
                    if (it.userState is Resource.Success) {
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            it.userState.data.ruasJalan.map { ruasJalan -> "${ruasJalan.namaRuas} - ${ruasJalan.id}" })
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
                    processStartSurveiState(it.startSurveiLubang)
                    processTambahLubangState(it.tambahLubang)
                    processKurangLubangState(it.kurangLubang)

                    setVisibilityFormEntry(it.isStarted)

                    binding.apply {
                        buttonStart.isEnabled = it.idRuasJalan != ""

                        textViewContentTanggal.text =
                            CalendarUtils.formatCalendarToString(it.tanggal)

                        textFieldRuasJalan.isEnabled = !it.isStarted
                        buttonPilihTanggal.isVisible = !it.isStarted
                        buttonStart.isVisible = !it.isStarted

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
                            it.isStarted && it.kodeLokasi.isNotBlank() && it.lokasiKm.isNotBlank() && it.lokasiM.isNotBlank() && it.panjangLubang > 0 && it.gambarLubangUri != null && it.lajur != null && it.ukuran != null && it.kedalaman != null

                        buttonTambahLubangGroup.isEnabled =
                            it.isStarted && it.kodeLokasi.isNotBlank() && it.lokasiKm.isNotBlank() && it.lokasiM.isNotBlank() && it.panjangLubang > 0 && it.gambarLubangUri != null && it.jumlahLubangPerGroup > 0 && it.lajur != null && it.ukuran != null && it.kedalaman != null

                        buttonKurangLubangSingle.isEnabled =
                            it.isStarted
                                    && it.kodeLokasi.isNotBlank()
                                    && it.lokasiKm.isNotBlank()
                                    && it.lokasiM.isNotBlank()
                                    && editTextTotalPanjangLubang.text.toString()
                                .getValueOrElse("0.0").toDouble() > 0
                                    && textViewJumlahLubangSingle.text.toString()
                                .getValueOrElse("0.0").toDouble() > 0
                    }

                }
            }
        }
    }

    private fun processStartSurveiState(state: Resource<SurveiLubang>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                showToast(state.errorMessage)
            }
            is Resource.Initial -> Unit
            is Resource.Loading -> {
                loadingDialog.show(childFragmentManager)
            }
            is Resource.Success -> {
                loadingDialog.dismiss()
                binding.apply {
                    textViewJumlahLubangSingle.text = state.data.jumlahTotal.toString()
                    editTextTotalPanjangLubang.setText(state.data.panjangTotal.toString())
                    surveiLubangViewModel.processAction(SurveiLubangAction.ResetStartState)
                }
            }
        }
    }

    private fun processTambahLubangState(state: Resource<SurveiLubang>) {
        when (state) {
            is Resource.Failed -> {
                percentageLoadingDialog.dismiss()
                showToast(state.errorMessage)
            }
            is Resource.Initial -> Unit
            is Resource.Loading -> {
                percentageLoadingDialog.show(childFragmentManager)
            }
            is Resource.Success -> {
                percentageLoadingDialog.dismiss()
                binding.apply {
                    Log.d(TAG, "TOTAL LUBANG: ${state.data.jumlahTotal}")
                    Log.d(TAG, "PANJANG LUBANG: ${state.data.panjangTotal}")
                    textViewJumlahLubangSingle.text = state.data.jumlahTotal.toString()
                    editTextTotalPanjangLubang.setText(state.data.panjangTotal.toString())
                }
                clearInputLubang()
                surveiLubangViewModel.processAction(SurveiLubangAction.ResetTambahState)
            }
        }
    }

    private fun processKurangLubangState(state: Resource<SurveiLubang>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                showToast(state.errorMessage)
            }
            is Resource.Initial -> Unit
            is Resource.Loading -> {
                loadingDialog.show(childFragmentManager)
            }
            is Resource.Success -> {
                loadingDialog.dismiss()
                binding.apply {
                    textViewJumlahLubangSingle.text = state.data.jumlahTotal.toString()
                    editTextTotalPanjangLubang.setText(state.data.panjangTotal.toString())
                    surveiLubangViewModel.processAction(SurveiLubangAction.ResetKurangState)
                }
            }
        }
    }
}