package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_rencana

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.AppNavigationDirections
import id.go.jabarprov.dbmpr.surveisapulubang.common.presentation.widget.SpaceItemDecoration
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryRencanaBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.adapter.LubangAdapter
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.RencanaViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store.RencanaAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.DetailLubangDialog
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch

private const val TAG = "EntryRencanaFragment"

@AndroidEntryPoint
class EntryRencanaFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    private val rencanaViewModel: RencanaViewModel by viewModels()

    private lateinit var binding: FragmentEntryRencanaBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

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

    private val lubangAdapter by lazy {
        LubangAdapter(LubangAdapter.TYPE.RENCANA)
            .setOnItemClickListener {
                selectedLubang = it
                rencanaDialog.show(childFragmentManager, "Rencana Dialog")
            }.setOnDetailItemClickListener {
                if (it.urlGambar != null) {
                    findNavController().navigate(
                        AppNavigationDirections.actionGlobalPreviewPhotoFragment(
                            it.urlGambar
                        )
                    )
                }
            }
    }

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private val timePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    rencanaViewModel.processAction(RencanaAction.UpdateTanggal(it))
                }
                addOnNegativeButtonClickListener {
                    dismiss()
                }
            }
    }

    lateinit var selectedLubang: Lubang

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryRencanaBinding.inflate(inflater, container, false)

        observeAuthState()
        observePenangananState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {

            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            editTextRuasJalan.doOnTextChanged { text, _, _, _ ->
                rencanaViewModel.processAction(RencanaAction.UpdateRuasJalan(text.toString()))
            }

            buttonPilihTanggal.setOnClickListener {
                timePicker.show(childFragmentManager, "Time Picker Dialog")
            }

            recyclerViewListLubang.apply {
                adapter = lubangAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
                setHasFixedSize(true)
            }

            buttonLoadData.setOnClickListener {
                rencanaViewModel.processAction(RencanaAction.LoadData)
            }
        }
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.uiState.collect {
                    if (it.user != null) {
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            it.user.ruasJalan.map { ruasJalan -> "${ruasJalan.namaRuas} - ${ruasJalan.id}" })
                        binding.editTextRuasJalan.setAdapter(adapter)
                    }
                }
            }
        }
    }

    private fun observePenangananState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                rencanaViewModel.uiState.collect {

                    if (it.isLoading) {
                        loadingDialog.show(childFragmentManager, "Loading Dialog")
                    }

                    if (it.isFailed) {
                        loadingDialog.dismiss()
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    if (it.isSuccess) {
                        loadingDialog.dismiss()
                        if (it.listLubang.isNotEmpty()) {
                            binding.recyclerViewListLubang.visibility = View.VISIBLE
                            binding.textViewEmpty.visibility = View.GONE
                            lubangAdapter.submitList(it.listLubang)
                        } else {
                            binding.recyclerViewListLubang.visibility = View.GONE
                            binding.textViewEmpty.visibility = View.VISIBLE
                        }
                    }

                    binding.apply {
                        textViewContentTanggal.text =
                            CalendarUtils.formatCalendarToString(it.tanggal)

                        buttonLoadData.isEnabled = it.idRuasJalan != ""
                    }
                }
            }
        }
    }

}