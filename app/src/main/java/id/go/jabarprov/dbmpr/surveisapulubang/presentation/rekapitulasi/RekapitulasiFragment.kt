package id.go.jabarprov.dbmpr.surveisapulubang.presentation.rekapitulasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentRekapitulasiBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.RekapitulasiViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store.RekapitulasiAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.runSafety
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
            textViewContentJumlahLubang.text = rekapitulasi.totalDataSurvei.toString()
            textViewContentJumlahRencana.text = rekapitulasi.totalPerencanaan.toString()
            textViewContentJumlahPenanganan.text = rekapitulasi.totalPenanganan.toString()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                rekapitulasiViewModel.uiState.collect {
                    if (it.isLoading) {
                        loadingDialog.show(childFragmentManager, "Loading Dialog")
                    }

                    if (it.isFailed) {
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        runSafety {
                            loadingDialog.dismiss()
                        }
                    }

                    if (it.isSuccess && it.rekapitulasi != null) {
                        runSafety {
                            loadingDialog.dismiss()
                        }
                        updateRekapitulasiUI(it.rekapitulasi)
                    }
                }
            }
        }
    }
}