package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_rencana

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.AppNavigationDirections
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.common.presentation.widget.SpaceItemDecoration
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryRencanaListLubangBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.adapter.LubangAdapter
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.RencanaViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store.RencanaAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.KonfirmasiDialog
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EntryRencanaListLubangFragment : Fragment() {

    private val loadingDialog by lazy { LoadingDialog.create() }

    private val rencanaViewModel: RencanaViewModel by hiltNavGraphViewModels(R.id.rencana_navigation)

    private val lubangAdapter by lazy {
        LubangAdapter(LubangAdapter.TYPE.RENCANA)
            .setOnDetailClickListener {
                if (it.urlGambar != null) {
                    findNavController().navigate(
                        AppNavigationDirections.actionGlobalPreviewPhotoFragment(
                            getSapuLubangImageUrl(it.urlGambar)
                        )
                    )
                } else {
                    showToast("Tidak ada foto")
                }
            }
            .setOnProsesClickListener {
                selectedLubang = it
                rencanaDialog.show(childFragmentManager, "Rencana Dialog")
            }
            .setOnDeleteClickListener {
                selectedLubang = it
                konfirmasiDialog.show(childFragmentManager, "Konfirmasi Dialog")
            }
    }

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private val konfirmasiDialog by lazy {
        KonfirmasiDialog.create(
            "Tolak Lubang",
            "Apakah anda yakin untuk menolak lubang ini?",
            onPositive = {
                rencanaViewModel.processAction(RencanaAction.RejectLubang(selectedLubang.id))
                it.dismiss()
            },
            onNegative = {
                it.dismiss()
            })
    }

    private val rencanaDialog by lazy {
        ConfirmationRencanaDialog.create(
            onPositiveButtonClickListener = { dialog, keteranganRencana ->
                rencanaViewModel.processAction(
                    RencanaAction.UploadRencanaLubang(
                        selectedLubang.id,
                        keteranganRencana
                    )
                )
                dialog.dismiss()
            },
            onNegativeButtonClickListener = {
                it.dismiss()
            },
        )
    }

    lateinit var selectedLubang: Lubang

    private lateinit var binding: FragmentEntryRencanaListLubangBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryRencanaListLubangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeRencanaState()
        loadLubangPerencanaan()
    }

    private fun initUI() {
        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            swipeRefeshLayout.setOnRefreshListener {
                loadLubangPerencanaan()
            }

            textViewRuasJalan.text = rencanaViewModel.uiState.value.ruasJalan

            recyclerViewListLubang.apply {
                adapter = lubangAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
            }
        }
    }

    private fun loadLubangPerencanaan() {
        rencanaViewModel.processAction(RencanaAction.GetListLubang)
    }

    private fun observeRencanaState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                rencanaViewModel.uiState.collect {
                    processLubangState(it.listLubang)
                    processRejectLubangState(it.rejectLubang)
                }
            }
        }
    }

    private fun processLubangState(state: Resource<List<Lubang>>) {
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
                if (!loadingDialog.isVisible && !loadingDialog.isAdded) {
                    loadingDialog.show(childFragmentManager, "Loading Penanganan Dialog")
                }
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

    private fun processRejectLubangState(state: Resource<Unit>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                showToast(state.errorMessage)
            }
            is Resource.Initial -> Unit
            is Resource.Loading -> {
                loadingDialog.show(childFragmentManager, "Loadin Dialog")
            }
            is Resource.Success -> {
                loadingDialog.dismiss()
                loadLubangPerencanaan()
            }
        }
    }

}