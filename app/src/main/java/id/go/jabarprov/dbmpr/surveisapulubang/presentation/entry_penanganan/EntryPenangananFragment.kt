package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_penanganan

import android.R
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.common.presentation.widget.SpaceItemDecoration
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryPenangananBinding
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.PenangananViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store.PenangananAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch

private const val TAG = "EntryPenangananFragment"

@AndroidEntryPoint
class EntryPenangananFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    private val penangananViewModel: PenangananViewModel by viewModels()

    private lateinit var binding: FragmentEntryPenangananBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

    private val spaceItemDecoration by lazy { SpaceItemDecoration(32) }

    private val unhandleLubangAdapter by lazy { UnhandleLubangAdapter() }

    private val timePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    penangananViewModel.processAction(PenangananAction.UpdateTanggal(it))
                }
                addOnNegativeButtonClickListener {
                    dismiss()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryPenangananBinding.inflate(inflater, container, false)

        observeAuthState()
        observePenangananState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            editTextRuasJalan.doOnTextChanged { text, _, _, _ ->
                penangananViewModel.processAction(PenangananAction.UpdateRuasJalan(text.toString()))
            }

            buttonPilihTanggal.setOnClickListener {
                timePicker.show(childFragmentManager, "Time Picker Dialog")
            }

            recyclerViewListLubang.apply {
                adapter = unhandleLubangAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(spaceItemDecoration)
                setHasFixedSize(true)
            }

            buttonSubmit.setOnClickListener {
                penangananViewModel.processAction(PenangananAction.GetListUnhandledLubang)
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
                            R.layout.simple_list_item_1,
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
                penangananViewModel.uiState.collect {

                    if (it.isLoading) {
                        loadingDialog.show(childFragmentManager, "Loading Dialog")
                    }

                    if (it.isFailed) {
                        loadingDialog.dismiss()
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    if (it.isSuccess) {
                        loadingDialog.dismiss()
                        unhandleLubangAdapter.submitList(it.listUnhandledLubang)
                    }

                    binding.apply {
                        textViewContentTanggal.text =
                            CalendarUtils.formatCalendarToString(it.tanggal)

                        buttonSubmit.isEnabled = it.idRuasJalan != ""
                    }
                }
            }
        }
    }

}