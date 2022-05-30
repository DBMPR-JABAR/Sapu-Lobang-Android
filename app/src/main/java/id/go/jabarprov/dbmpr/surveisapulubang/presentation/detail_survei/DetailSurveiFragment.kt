package id.go.jabarprov.dbmpr.surveisapulubang.presentation.detail_survei

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
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentDetailSurveiBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.ResultSurvei
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.DetailSurveiViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store.DetailSurveiAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import kotlinx.coroutines.launch

private const val TAG = "DetailSurveiFragment"

@AndroidEntryPoint
class DetailSurveiFragment : Fragment() {

    private val detailSurveiViewModel: DetailSurveiViewModel by viewModels()

    private lateinit var binding: FragmentDetailSurveiBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

    private val detailSurveiListLubangFragment by lazy { DetailSurveiLubangFragment() }
    private val detailSurveiListPotensiFragment by lazy { DetailSurveiPotensiFragment() }

    private var currentFragment: Fragment = detailSurveiListLubangFragment

    private val args: DetailSurveiFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailSurveiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        initChildFragment()
        observeDetailSurveiViewModel()
        loadDataSurvei()
    }

    private fun initUI() {
        binding.apply {
            swipeRefeshLayoutHasilSurvei.setOnRefreshListener {
                loadDataSurvei()
            }

            tabLayoutCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            navigateFragment(detailSurveiListLubangFragment)
                        }
                        1 -> {
                            navigateFragment(detailSurveiListPotensiFragment)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

                override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            })

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initChildFragment() {
        childFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment_container, detailSurveiListPotensiFragment)
            .hide(detailSurveiListPotensiFragment)
            .add(R.id.frame_layout_fragment_container, detailSurveiListLubangFragment)
            .commit()
    }

    private fun navigateFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .hide(currentFragment).apply {
                currentFragment = fragment
            }
            .show(fragment)
            .commit()
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
                    processLoadSurveiState(it.resultSurveiState)
                    processDeleteItemState(it.deleteItemState)
                }
            }
        }
    }

    private fun processLoadSurveiState(state: Resource<ResultSurvei>) {
        when (state) {
            is Resource.Failed -> {
                binding.swipeRefeshLayoutHasilSurvei.isRefreshing = false
                loadingDialog.dismiss()
            }
            is Resource.Initial -> Unit
            is Resource.Loading -> {
                loadingDialog.show(childFragmentManager, "Loading Dialog")
            }
            is Resource.Success -> {
                loadingDialog.dismiss()
                binding.apply {
                    swipeRefeshLayoutHasilSurvei.isRefreshing = false
                    textViewRuasJalan.text = state.data.ruas.namaRuas
                }
            }
        }
    }

    private fun processDeleteItemState(state: Resource<None>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
                showToast(state.errorMessage)
            }
            is Resource.Initial -> Unit
            is Resource.Loading -> {
                loadingDialog.show(childFragmentManager, "Loading Dialog")
            }
            is Resource.Success -> {
                loadingDialog.dismiss()
                loadDataSurvei()
            }
        }
    }
}