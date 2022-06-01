package id.go.jabarprov.dbmpr.surveisapulubang.presentation.rekapitulasi.penanganan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.AppNavigationDirections
import id.go.jabarprov.dbmpr.surveisapulubang.common.presentation.widget.SpaceItemDecoration
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentRekapPenangananBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.adapter.LubangAdapter
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.penanganan.RekapPenangananViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.penanganan.store.RekapPenangananAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RekapPenangananFragment : Fragment() {

    private val rekapPenangananViewModel: RekapPenangananViewModel by viewModels()

    private val lubangAdapter by lazy {
        LubangAdapter(LubangAdapter.TYPE.DEFAULT)
            .setOnDetailClickListener {
                if (it.urlGambarPenanganan != null && it.urlGambarPenanganan.isNotBlank()) {
                    findNavController().navigate(
                        AppNavigationDirections.actionGlobalPreviewPhotoFragment(
                            getSapuLubangImageUrl(it.urlGambarPenanganan)
                        )
                    )
                } else {
                    showToast("Tidak ada foto")
                }
            }
    }

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private val loadingDialog by lazy { LoadingDialog.create() }

    private lateinit var binding: FragmentRekapPenangananBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRekapPenangananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeRekapPenangananState()
        loadPenanganan()
    }

    private fun initUI() {
        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            swipeRefeshLayout.setOnRefreshListener {
                loadPenanganan()
            }

            recyclerViewListPenanganan.apply {
                adapter = lubangAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
            }
        }
    }

    private fun loadPenanganan() {
        rekapPenangananViewModel.processAction(RekapPenangananAction.GetRekapPenanganan)
    }

    private fun observeRekapPenangananState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                rekapPenangananViewModel.uiState.collect {
                    processRekapPenangananState(it.rekapListPenanganan)
                }
            }
        }
    }

    private fun processRekapPenangananState(state: Resource<List<Lubang>>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                binding.apply {
                    swipeRefeshLayout.isRefreshing = false
                    recyclerViewListPenanganan.isVisible = false
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
                        recyclerViewListPenanganan.isVisible = false
                        textViewEmpty.isVisible = true
                        textViewError.isVisible = false
                    } else {
                        recyclerViewListPenanganan.isVisible = true
                        textViewEmpty.isVisible = false
                        textViewError.isVisible = false
                    }
                }
                lubangAdapter.submitList(state.data)
            }
        }
    }

}