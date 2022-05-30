package id.go.jabarprov.dbmpr.surveisapulubang.presentation.detail_survei

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
import id.go.jabarprov.dbmpr.surveisapulubang.AppNavigationDirections
import id.go.jabarprov.dbmpr.surveisapulubang.common.presentation.widget.SpaceItemDecoration
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentListLubangBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.ResultSurvei
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.DetailSurveiViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store.DetailSurveiAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.KonfirmasiDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl
import kotlinx.coroutines.launch


class DetailSurveiLubangFragment : Fragment() {

    private val detailSurveiViewModel: DetailSurveiViewModel by viewModels({ requireParentFragment() })

    private val resultSurveiListLubangAdapter by lazy {
        ResultSurveiLubangAdapter()
            .setOnDetailButtonClickListener {
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
            .setOnDeleteButtonClickListener {
                selectedSurveiItem = it
                konfirmasiDialog.show(childFragmentManager, "Konfirmasi Dialog")
            }
    }

    private val konfirmasiDialog by lazy {
        KonfirmasiDialog.create(
            title = "Konfirmasi Penghapusan",
            description = "Apakah anda yakin untuk menghapus lubang ini?",
            onNegative = { it.dismiss() },
            onPositive = {
                detailSurveiViewModel.processAction(
                    DetailSurveiAction.DeleteLubang(
                        selectedSurveiItem.id
                    )
                )
                it.dismiss()
            }
        )
    }

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private lateinit var binding: FragmentListLubangBinding

    private lateinit var selectedSurveiItem: Lubang

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListLubangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeDetailSurveiState()
    }

    private fun initUI() {
        binding.apply {
            recyclerViewListLubang.apply {
                adapter = resultSurveiListLubangAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
            }
        }
    }

    private fun observeDetailSurveiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                detailSurveiViewModel.uiState.collect {
                    processResultSurveiState(it.resultSurveiState)
                }
            }
        }
    }

    private fun processResultSurveiState(state: Resource<ResultSurvei>) {
        when (state) {
            is Resource.Failed -> {
                binding.apply {
                    recyclerViewListLubang.isVisible = false
                    textViewEmpty.isVisible = false
                    textViewError.apply {
                        text = state.errorMessage
                        isVisible = true
                    }
                }
            }
            is Resource.Success -> {
                if (state.data.listLubang.isEmpty()) {
                    binding.apply {
                        recyclerViewListLubang.isVisible = false
                        textViewError.isVisible = false
                        textViewEmpty.isVisible = true
                    }
                } else {
                    binding.apply {
                        recyclerViewListLubang.isVisible = true
                        textViewError.isVisible = false
                        textViewEmpty.isVisible = false
                    }
                }
                resultSurveiListLubangAdapter.submitList(state.data.listLubang)
            }
            else -> Unit
        }
    }

}