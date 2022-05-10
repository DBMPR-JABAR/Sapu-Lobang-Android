package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_penanganan

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.AppNavigationDirections
import id.go.jabarprov.dbmpr.surveisapulubang.common.presentation.widget.SpaceItemDecoration
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryPenangananBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.adapter.LubangAdapter
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.PenangananViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store.PenangananAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store.SurveiLubangAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.LocationUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl
import kotlinx.coroutines.launch

private const val TAG = "EntryPenangananFragment"

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class EntryPenangananFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    private val penangananViewModel: PenangananViewModel by viewModels()

    private lateinit var binding: FragmentEntryPenangananBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

    private val locationUtils by lazy { LocationUtils(requireActivity()) }

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
        }
    }

    private var confirmationDialog = createConfirmationDialog()

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private val lubangAdapter by lazy {
        LubangAdapter(LubangAdapter.TYPE.PENANGANAN)
            .setOnItemClickListener { lubang ->
                selectedLubang = lubang
                confirmationDialog = createConfirmationDialog()
                confirmationDialog.apply {
                    if (lubang.keterangan != null) {
                        setKeterangan(lubang.keterangan)
                    }
                }.show(childFragmentManager, "Penanganan Dialog")
            }.setOnDetailItemClickListener { lubang ->
                when {
                    lubang.urlGambarPenanganan != null -> {
                        findNavController().navigate(
                            AppNavigationDirections.actionGlobalPreviewPhotoFragment(
                                getSapuLubangImageUrl(lubang.urlGambarPenanganan)
                            )
                        )
                    }
                    lubang.urlGambar != null -> {
                        findNavController().navigate(
                            AppNavigationDirections.actionGlobalPreviewPhotoFragment(
                                getSapuLubangImageUrl(lubang.urlGambar)
                            )
                        )
                    }
                    else -> {
                        showToast("Tidak ada gambar")
                    }
                }
            }
    }

    private val timePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    penangananViewModel.processAction(PenangananAction.UpdateTanggal(it))
                }
                addOnNegativeButtonClickListener {
                    dismiss()
                }
            }
    }

    lateinit var selectedLubang: Lubang

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryPenangananBinding.inflate(inflater, container, false)

        observeAuthState()
        observePenangananState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkLocationPermission()
        initUI()
    }

    @SuppressLint("MissingPermission")
    private fun initUI() {
        binding.apply {

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            editTextRuasJalan.doOnTextChanged { text, _, _, _ ->
                penangananViewModel.processAction(PenangananAction.UpdateRuasJalan(text.toString()))
            }

            buttonPilihTanggal.setOnClickListener {
                timePicker.show(childFragmentManager, "Time Picker Dialog")
            }

            recyclerViewListLubang.apply {
                adapter = lubangAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
                setHasFixedSize(true)
            }

            buttonSubmit.setOnClickListener {
                penangananViewModel.processAction(PenangananAction.GetListLubang)
            }
        }
    }

    private fun createConfirmationDialog(): ConfirmationPenangananDialog {
        return ConfirmationPenangananDialog.create(
            onPositiveButtonClickListener = { dialog, keteranganPenanganan, gambarPenanganan, _ ->
                lifecycleScope.launchWhenResumed {
                    penangananViewModel.processAction(PenangananAction.GetLocation)
                    val location = locationUtils.getCurrentLocation(CancellationTokenSource().token)
                    if (location == null) {
                        penangananViewModel.processAction(PenangananAction.GetLocationFailed("Gagal Mengambil Lokasi"))
                    } else {
                        penangananViewModel.processAction(
                            PenangananAction.StorePenangananLubang(
                                selectedLubang.id,
                                keteranganPenanganan,
                                gambarPenanganan,
                                location.latitude,
                                location.longitude
                            )
                        )
                    }
                }
                dialog.dismiss()
            },
            onNegativeButtonClickListener = { dialog ->
                dialog.dismiss()
            }
        )
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.uiState.collect {
                    if (it.user != null) {
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.simple_list_item_1,
                            it.user.ruasJalan.map { ruasJalan -> "${ruasJalan.namaRuas} - ${ruasJalan.id}" })
                        binding.editTextRuasJalan.setAdapter(adapter)
                    }
                }
            }
        }
    }

    private fun observePenangananState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                penangananViewModel.uiState.collect {

                    if (it.isLoading) {
                        loadingDialog.show(childFragmentManager, "Loading Dialog")
                    }

                    if (it.isFailed) {
                        loadingDialog.dismiss()
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        penangananViewModel.processAction(PenangananAction.ResetStatePenanganan)
                    }

                    if (it.isSuccess) {
                        loadingDialog.dismiss()
                        if (it.listLubang.isNotEmpty()) {
                            binding.recyclerViewListLubang.visibility = View.VISIBLE
                            binding.textViewEmpty.visibility = View.GONE
                            lubangAdapter.submitList(it.listLubang)
                        } else {
                            binding.recyclerViewListLubang.visibility = View.GONE
                            binding.textViewEmpty.visibility = View.VISIBLE
                        }
                    }

                    binding.apply {
                        textViewContentTanggal.text =
                            CalendarUtils.formatCalendarToString(it.tanggal)

                        buttonSubmit.isEnabled = it.idRuasJalan != ""
                    }
                }
            }
        }
    }

}