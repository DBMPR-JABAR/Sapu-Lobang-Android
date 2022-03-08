package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_lubang

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.LocationServices
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryLubangBinding
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.SurveiLubangViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store.SurveiLubangAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.LocationUtils
import kotlinx.coroutines.launch

private const val TAG = "EntryLubangFragment"

@AndroidEntryPoint
class EntryLubangFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    private val surveiLubangViewModel: SurveiLubangViewModel by viewModels()

    private val requestLocationPermissionLauncher by lazy {
        registerForActivityResult(
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
        checkLocationPermission()

        binding.apply {
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

            buttonStart.setOnClickListener {
                surveiLubangViewModel.processAction(
                    SurveiLubangAction.StartSurveiAction(
                        surveiLubangViewModel.uiState.value.tanggal,
                        surveiLubangViewModel.uiState.value.idRuasJalan
                    )
                )
            }



            buttonTambah.setOnClickListener {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.TambahLubangAction(
                            it.latitude,
                            it.longitude,
                        )
                    )
                }
            }

            buttonKurang.setOnClickListener {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    surveiLubangViewModel.processAction(
                        SurveiLubangAction.KurangLubangAction(
                            it.latitude,
                            it.longitude,
                        )
                    )
                }
            }
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
                    Log.d(TAG, "observeState: $it")

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

                    binding.apply {
                        buttonStart.isEnabled = it.idRuasJalan != ""
                        textViewContentTanggal.text =
                            CalendarUtils.formatCalendarToString(it.tanggal)

                        textFieldRuasJalan.isVisible = !it.isStarted
                        buttonPilihTanggal.isVisible = !it.isStarted
                        buttonStart.isVisible = !it.isStarted

                        textViewLabelKm.isVisible = it.isStarted
                        textFieldKodeLokasi.isVisible = it.isStarted
                        textFieldLokasiKm.isVisible = it.isStarted
                        textViewSymbolTambah.isVisible = it.isStarted
                        textFieldLokasiM.isVisible = it.isStarted

                        textViewJumlahLubang.isVisible = it.isStarted
                        buttonTambah.isVisible = it.isStarted
                        buttonKurang.isVisible = it.isStarted

                        textViewLabelRuasJalan.isVisible = it.isStarted
                        textViewContentRuasJalan.isVisible = it.isStarted
                        textViewContentRuasJalan.text = it.ruasJalan

                        textViewJumlahLubang.text = it.jumlahLubang.toString()

                        buttonTambah.isEnabled =
                            it.isStarted && it.kodeLokasi.isNotBlank() && it.lokasiKm.isNotBlank() && it.lokasiM.isNotBlank()

                        buttonKurang.isEnabled =
                            it.isStarted && it.kodeLokasi.isNotBlank() && it.lokasiKm.isNotBlank() && it.lokasiM.isNotBlank() && it.jumlahLubang > 0
                    }
                }
            }
        }
    }
}