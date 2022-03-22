package id.go.jabarprov.dbmpr.surveisapulubang.presentation.detail_survei

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentDetailSurveiBinding
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.DetailSurveiViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store.DetailSurveiAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch

private const val TAG = "DetailSurveiFragment"

@AndroidEntryPoint
class DetailSurveiFragment : Fragment() {

    private val detailSurveiViewModel: DetailSurveiViewModel by viewModels()

    private lateinit var binding: FragmentDetailSurveiBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

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
        observeDetailSurveiViewModel()
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
                        Log.d(TAG, "observeDetailSurveiViewModel: ${it.listLubang}")
                    }

                    if (it.isFailed) {
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        loadingDialog.dismiss()
                    }
                }
            }
        }
    }
}