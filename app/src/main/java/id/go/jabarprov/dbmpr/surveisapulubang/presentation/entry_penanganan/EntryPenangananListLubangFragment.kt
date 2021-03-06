package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_penanganan

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.AppNavigationDirections
import id.go.jabarprov.dbmpr.surveisapulubang.PenangananNavigationDirections
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.common.presentation.widget.SpaceItemDecoration
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryPenangananListLubangBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.adapter.LubangAdapter
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.map.MapFragment
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.PenangananViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store.PenangananAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.PercentageLoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.LocationUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EntryPenangananListLubangFragment : Fragment() {

    private val penangananViewModel: PenangananViewModel by hiltNavGraphViewModels(R.id.penanganan_navigation)

    private val loadingDialog by lazy { LoadingDialog.create() }

    private val percentageLoadingDialog by lazy { PercentageLoadingDialog.create() }

    private lateinit var binding: FragmentEntryPenangananListLubangBinding

    private val locationUtils by lazy { LocationUtils(requireActivity()) }

    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                currentLocation = location.lastLocation
            }
        }
    }

    private var isRequestingLocation = false
    private var currentLocation: Location? = null

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION,
                false
            ) -> {
                if (!isRequestingLocation) {
                    setUpLocationSetting()
                }
            }
            permissions.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                false
            ) -> {
                if (!isRequestingLocation) {
                    setUpLocationSetting()
                }
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
        locationUtils.addLocationListener(locationCallback)
        isRequestingLocation = true
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
            if (!isRequestingLocation) {
                setUpLocationSetting()
            }
        }
    }

    private var confirmationDialog = createConfirmationDialog()

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private val lubangAdapter by lazy {
        LubangAdapter(LubangAdapter.TYPE.PENANGANAN)
            .setOnProsesClickListener { lubang ->
                selectedLubang = lubang
                confirmationDialog = createConfirmationDialog()
                confirmationDialog.apply {
                    if (lubang.keterangan != null) {
                        setKeterangan(lubang.keterangan)
                    }
                }.show(childFragmentManager, "Penanganan Dialog")
            }.setOnDetailClickListener { lubang ->
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
            .setOnCheckOnMapClickListener {
                val action = PenangananNavigationDirections.actionGlobalMapFragment(
                    MapFragment.PointLubang(
                        it.latitude,
                        it.longitude
                    )
                )
                findNavController().navigate(action)
            }
    }

    lateinit var selectedLubang: Lubang

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryPenangananListLubangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observePenangananState()
        loadLubangPenanganan()
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    private fun initUI() {
        binding.apply {
            textViewRuasJalan.text = penangananViewModel.uiState.value.ruasJalan

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            swipeRefeshLayout.setOnRefreshListener {
                loadLubangPenanganan()
            }

            recyclerViewListLubang.apply {
                adapter = lubangAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
            }
        }
    }

    private fun loadLubangPenanganan() {
        penangananViewModel.processAction(PenangananAction.GetListLubang)
    }

    private fun createConfirmationDialog(): ConfirmationPenangananDialog {
        return ConfirmationPenangananDialog.create(
            onPositiveButtonClickListener = { dialog, keteranganPenanganan, gambarPenanganan, _ ->
                lifecycleScope.launchWhenResumed {
                    if (currentLocation == null) {
                        penangananViewModel.processAction(PenangananAction.GetLocationFailed("Gagal Mengambil Lokasi"))
                    } else {
                        penangananViewModel.processAction(
                            PenangananAction.StorePenangananLubang(
                                selectedLubang.id,
                                keteranganPenanganan,
                                gambarPenanganan,
                                currentLocation!!.latitude,
                                currentLocation!!.longitude
                            ) {
                                percentageLoadingDialog.updateProgress(it)
                            }
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

    private fun observePenangananState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                penangananViewModel.uiState.collect {
                    processListLubangState(it.listLubang)
                    processStoreLubangState(it.storePenanganan)
                    processLocationState(it.location)
                }
            }
        }
    }

    private fun processListLubangState(state: Resource<List<Lubang>>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                binding.apply {
                    swipeRefeshLayout.isRefreshing = false
                    recyclerViewListLubang.isVisible = false
                    textViewEmpty.isVisible = false
                    textViewError.apply {
                        isVisible = true
                        text = state.errorMessage
                    }
                }
            }
            is Resource.Initial -> Unit
            is Resource.Loading -> {
                loadingDialog.show(childFragmentManager)
            }
            is Resource.Success -> {
                loadingDialog.dismiss()
                binding.apply {
                    swipeRefeshLayout.isRefreshing = false
                    if (state.data.isEmpty()) {
                        recyclerViewListLubang.isVisible = false
                        textViewEmpty.isVisible = true
                        textViewError.isVisible = false
                    } else {
                        recyclerViewListLubang.isVisible = true
                        textViewEmpty.isVisible = false
                        textViewError.isVisible = false
                    }
                }
                lubangAdapter.submitList(state.data)
            }
        }
    }

    private fun processStoreLubangState(state: Resource<List<Lubang>>) {
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
                lubangAdapter.submitList(state.data)
            }
        }
    }

    private fun processLocationState(state: Resource<Unit>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                showToast(state.errorMessage)
            }
            is Resource.Loading -> {
                loadingDialog.show(childFragmentManager)
            }
            else -> Unit
        }
    }

    override fun onPause() {
        if (isRequestingLocation) {
            locationUtils.removeLocationListener(locationCallback)
            isRequestingLocation = false
        }
        super.onPause()
    }
}