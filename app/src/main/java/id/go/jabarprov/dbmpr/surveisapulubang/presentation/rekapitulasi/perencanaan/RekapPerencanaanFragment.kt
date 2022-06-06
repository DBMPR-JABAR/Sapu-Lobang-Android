package id.go.jabarprov.dbmpr.surveisapulubang.presentation.rekapitulasi.perencanaan

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
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentRekapPerencanaanBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.adapter.LubangAdapter
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.perencanaan.RekapPerencanaanViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.perencanaan.store.RekapPerencanaanAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RekapPerencanaanFragment : Fragment() {

    private val rekapPerencanaanViewModel: RekapPerencanaanViewModel by viewModels()

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

    private val loadingDialog by lazy { LoadingDialog.create() }

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private lateinit var binding: FragmentRekapPerencanaanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRekapPerencanaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeRekapPerencanaanState()
        loadPerencanaan()
    }

    private fun initUI() {
        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            swipeRefeshLayout.setOnRefreshListener {
                loadPerencanaan()
            }

            recyclerViewListPerencanaan.apply {
                adapter = lubangAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
            }
        }
    }

    private fun loadPerencanaan() {
        rekapPerencanaanViewModel.processAction(RekapPerencanaanAction.GetRekapPerencanaan)
    }

    private fun observeRekapPerencanaanState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                rekapPerencanaanViewModel.uiState.collect {
                    processRekapPerencanaanState(it.rekapListPerencanaan)
                }
            }
        }
    }

    private fun processRekapPerencanaanState(state: Resource<List<Lubang>>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                binding.apply {
                    swipeRefeshLayout.isRefreshing = false
                    recyclerViewListPerencanaan.isVisible = false
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
                        recyclerViewListPerencanaan.isVisible = false
                        textViewEmpty.isVisible = true
                        textViewError.isVisible = false
                    } else {
                        recyclerViewListPerencanaan.isVisible = true
                        textViewEmpty.isVisible = false
                        textViewError.isVisible = false
                    }
                }
                lubangAdapter.submitList(state.data)
            }
        }
    }

}