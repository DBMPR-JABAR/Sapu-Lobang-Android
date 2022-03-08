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
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryRencanaBinding
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.RencanaViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store.RencanaAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store.SurveiLubangAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.addTextWatcherWithReplacement
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EntryRencanaFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    private val rencanaViewModel: RencanaViewModel by viewModels()

    private lateinit var binding: FragmentEntryRencanaBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

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
            editTextJumlahRencana.setText(rencanaViewModel.uiState.value.jumlahPenanganan.toString())

            editTextRuasJalan.doOnTextChanged { text, _, _, _ ->
                rencanaViewModel.processAction(RencanaAction.UpdateRuasJalan(text.toString()))
            }

            editTextJumlahRencana.addTextWatcherWithReplacement {
                var text = it.toString()

                if (it.isNullOrBlank()) {
                    text = "0"
                    editTextJumlahRencana.setText(text)
                    editTextJumlahRencana.setSelection(it.toString().length + 1)
                }

                if (it.toString().startsWith("0")) {
                    text = it?.subSequence(1, it.toString().length).toString()
                    editTextJumlahRencana.setText(text)
                    editTextJumlahRencana.setSelection(it.toString().length - 1)
                }

                rencanaViewModel.processAction(
                    RencanaAction.UpdateJumlahRencanaPenanganan(text.toInt())
                )
            }

            buttonPilihTanggal.setOnClickListener {
                timePicker.show(childFragmentManager, "Time Picker Dialog")
            }

            buttonSubmit.setOnClickListener {
                rencanaViewModel.processAction(RencanaAction.UploadRencanaPenanganan)
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
                        findNavController().popBackStack()
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