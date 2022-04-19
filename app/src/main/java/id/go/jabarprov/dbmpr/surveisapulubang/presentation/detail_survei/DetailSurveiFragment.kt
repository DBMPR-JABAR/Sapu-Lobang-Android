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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.AppNavigationDirections
import id.go.jabarprov.dbmpr.surveisapulubang.common.presentation.widget.SpaceItemDecoration
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentDetailSurveiBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.DetailSurveiViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store.DetailSurveiAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.KonfirmasiDialog
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl
import kotlinx.coroutines.launch

private const val TAG = "DetailSurveiFragment"

@AndroidEntryPoint
class DetailSurveiFragment : Fragment() {

    private val detailSurveiViewModel: DetailSurveiViewModel by viewModels()

    private lateinit var binding: FragmentDetailSurveiBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

    private val konfirmasiDialog by lazy {
        KonfirmasiDialog.create(
            title = "Konfirmasi Penghapusan",
            description = "Apakah anda yakin untuk menghapus lubang ini?",
            onNegative = { it.dismiss() },
            onPositive = {
                detailSurveiViewModel.processAction(
                    if (selectedSurveiItem.potensi) {
                        DetailSurveiAction.DeletePotensiLubang(
                            selectedSurveiItem.id
                        )
                    } else {
                        DetailSurveiAction.DeleteLubang(
                            selectedSurveiItem.id
                        )
                    }
                )
                it.dismiss()
            }
        )
    }

    private lateinit var selectedSurveiItem: Lubang

    private val resultSurveiAdapter by lazy {
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

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private val args: DetailSurveiFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailSurveiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadDataSurvei()
        initUI()
        observeDetailSurveiViewModel()
    }

    private fun initUI() {
        binding.apply {
            swipeRefeshLayoutHasilSurvei.setOnRefreshListener {
                loadDataSurvei()
            }

            recyclerViewListLubang.apply {
                adapter = resultSurveiAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
                setHasFixedSize(true)
            }

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun loadDataSurvei() {
        detailSurveiViewModel.processAction(
            DetailSurveiAction.LoadSurveiData(
                args.idRuasJalan,
                CalendarUtils.formatStringToCalendar(args.tanggal)
            )
        )
    }

    private fun observeDetailSurveiViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailSurveiViewModel.uiState.collect {
                    if (it.isLoading) {
                        loadingDialog.show(childFragmentManager, "Loading Dialog")
                    }

                    if (it.isSuccess) {
                        loadingDialog.dismiss()
                        binding.swipeRefeshLayoutHasilSurvei.isRefreshing = false
                        if (it.listLubang.isNullOrEmpty()) {
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
                                resultSurveiAdapter.submitList(it.listLubang)
                            }
                        }
                    }

                    if (it.isFailed) {
                        binding.swipeRefeshLayoutHasilSurvei.isRefreshing = false
                        binding.apply {
                            recyclerViewListLubang.isVisible = false
                            textViewEmpty.isVisible = false
                            textViewError.text = it.errorMessage
                            textViewError.isVisible = true
                        }
                        loadingDialog.dismiss()
                    }

                    if (it.isDelete) {
                        loadDataSurvei()
                    }
                }
            }
        }
    }
}