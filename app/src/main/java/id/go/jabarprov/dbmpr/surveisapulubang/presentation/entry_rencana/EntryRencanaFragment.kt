package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_rencana

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentEntryRencanaBinding
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.RencanaViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store.RencanaAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EntryRencanaFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    private val rencanaViewModel: RencanaViewModel by hiltNavGraphViewModels(R.id.rencana_navigation)

    private lateinit var binding: FragmentEntryRencanaBinding

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeAuthState()
        observeRencanaState()
    }

    private fun initUI() {
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

            buttonLoadData.setOnClickListener {
                val action =
                    EntryRencanaFragmentDirections.actionEntryRencanaFragmentToEntryRencanaListLubangFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.uiState.collect {
                    if (it.userState is Resource.Success) {
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            it.userState.data.ruasJalan.map { ruasJalan -> "${ruasJalan.namaRuas} - ${ruasJalan.id}" })
                        binding.editTextRuasJalan.setAdapter(adapter)
                    }
                }
            }
        }
    }

    private fun observeRencanaState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                rencanaViewModel.uiState.collect {
                    binding.buttonLoadData.isVisible = it.ruasJalan.isNotBlank()
                }
            }
        }
    }

}