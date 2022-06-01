package id.go.jabarprov.dbmpr.surveisapulubang.presentation.rekapitulasi.kerusakan

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
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentRekapKerusakanPotensiBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.adapter.LubangAdapter
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.kerusakan.RekapKerusakanViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.kerusakan.store.RekapKerusakanAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RekapKerusakanPotensiFragment : Fragment() {

    private val rekapitulasiViewModel: RekapKerusakanViewModel by viewModels({
        requireParentFragment()
    })

    private val lubangAdapter by lazy {
        LubangAdapter(LubangAdapter.TYPE.DEFAULT).setOnDetailClickListener {
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
    }

    private val loadingDialog by lazy { LoadingDialog.create() }

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private lateinit var binding: FragmentRekapKerusakanPotensiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRekapKerusakanPotensiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeRekaptate()
        loadRekapPotensi()
    }

    private fun initUI() {
        binding.apply {
            swipeRefeshLayout.setOnRefreshListener {
                loadRekapPotensi()
            }

            recyclerViewListPotensi.apply {
                adapter = lubangAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
            }
        }
    }

    private fun loadRekapPotensi() {
        rekapitulasiViewModel.processAction(RekapKerusakanAction.GetRekapPotensi)
    }

    private fun observeRekaptate() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                rekapitulasiViewModel.uiState.collect {
                    processRekapPotensiState(it.rekapListPotensi)
                }
            }
        }
    }

    private fun processRekapPotensiState(state: Resource<List<Lubang>>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                binding.apply {
                    swipeRefeshLayout.isRefreshing = false
                    recyclerViewListPotensi.isVisible = false
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
                    loadingDialog.show(childFragmentManager, "Loading Potensi Dialog")
                }
            }
            is Resource.Success -> {
                loadingDialog.dismiss()
                binding.apply {
                    swipeRefeshLayout.isRefreshing = false
                    if (state.data.isEmpty()) {
                        recyclerViewListPotensi.isVisible = false
                        textViewEmpty.isVisible = true
                        textViewError.isVisible = false
                    } else {
                        recyclerViewListPotensi.isVisible = true
                        textViewEmpty.isVisible = false
                        textViewError.isVisible = false
                    }
                }
                lubangAdapter.submitList(state.data)
            }
        }
    }

}