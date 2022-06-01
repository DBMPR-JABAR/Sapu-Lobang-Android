package id.go.jabarprov.dbmpr.surveisapulubang.presentation.rekapitulasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentRekapitulasiBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.RekapitulasiViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store.RekapitulasiAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RekapitulasiFragment : Fragment() {

    private lateinit var binding: FragmentRekapitulasiBinding

    private val rekapitulasiViewModel by viewModels<RekapitulasiViewModel>()

    private val loadingDialog by lazy { LoadingDialog.create() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRekapitulasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        observeState()
        getRekapitulasi()
    }

    private fun initUI() {
        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getRekapitulasi() {
        rekapitulasiViewModel.processAction(RekapitulasiAction.GetRekapitulasi)
    }

    private fun updateRekapitulasiUI(rekapitulasi: Rekapitulasi) {
        binding.apply {
            constraintLayoutRekapKerusakan.setOnClickListener {
                val action =
                    RekapitulasiFragmentDirections.actionRekapitulasiFragmentToRekapKerusakanFragment()
                findNavController().navigate(action)
            }

            constraintLayoutRekapPerencanaan.setOnClickListener {
                val action =
                    RekapitulasiFragmentDirections.actionRekapitulasiFragmentToRekapPerencanaanFragment()
                findNavController().navigate(action)
            }

            constraintLayoutRekapPenanganan.setOnClickListener {
                val action =
                    RekapitulasiFragmentDirections.actionRekapitulasiFragmentToRekapPenangananFragment()
                findNavController().navigate(action)
            }

            textViewContentTotalLubang.text = rekapitulasi.jumlah.sisa.toString()
            textViewContentPanjangLubang.text = "${rekapitulasi.panjang.sisa} KM"
            textViewContentTotalPotensiLubang.text = rekapitulasi.jumlah.potensi.toString()
            textViewContentPanjangPotensiLubang.text = "${rekapitulasi.panjang.potensi} KM"
            textViewContentTotalPerencanaan.text = rekapitulasi.jumlah.perencanaan.toString()
            textViewContentTotalPanjangPerencanaan.text = "${rekapitulasi.panjang.perencanaan} KM"
            textViewContentTotalDitangani.text = rekapitulasi.jumlah.penanganan.toString()
            textViewContentTotalPanjangDitangani.text = "${rekapitulasi.panjang.penanganan} KM"
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                rekapitulasiViewModel.uiState.collect {
                    processRekapState(it.rekapState)
                }
            }
        }
    }

    private fun processRekapState(rekapState: Resource<Rekapitulasi>) {
        when (rekapState) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                showToast(rekapState.errorMessage)
            }
            is Resource.Initial -> Unit
            is Resource.Loading -> {
                loadingDialog.show(childFragmentManager, "Loading Dialog")
            }
            is Resource.Success -> {
                loadingDialog.dismiss()
                updateRekapitulasiUI(rekapState.data)
            }
        }
    }
}