package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_rencana

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutDialogKonfirmasiRencanaBinding
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import java.util.*

class ConfirmationRencanaDialog : DialogFragment() {

    lateinit var positiveClickListener: (DialogFragment, Calendar, String) -> Unit

    lateinit var negativeClickListener: (DialogFragment) -> Unit

    lateinit var binding: LayoutDialogKonfirmasiRencanaBinding

    private val datePickerDialog by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal Perencanaan")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    selectedDate = CalendarUtils.formatTimeInMilliesToCalendar(it)
                    dismiss()
                }
                addOnNegativeButtonClickListener {
                    dismiss()
                }
            }
    }

    var selectedDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDialogKonfirmasiRencanaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            buttonIya.setOnClickListener {
                positiveClickListener(
                    this@ConfirmationRencanaDialog,
                    selectedDate,
                    editTextPenanganan.text.toString()
                )
            }

            buttonBatal.setOnClickListener {
                negativeClickListener(this@ConfirmationRencanaDialog)
            }

            constraintLayoutTanggal.setOnClickListener {
                datePickerDialog.show(childFragmentManager, "Date Picker Dialog")
            }

            textViewContentTanggal.text = CalendarUtils.formatCalendarToString(selectedDate)

            editTextPenanganan.doOnTextChanged { text, _, _, _ ->
                buttonIya.isEnabled = !text.isNullOrBlank()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setOnPositiveClickListener(action: (dialog: DialogFragment, tanggal: Calendar, keteranganRencana: String) -> Unit) {
        positiveClickListener = action
    }

    private fun setOnNegativeButtonClickListener(action: (dialog: DialogFragment) -> Unit) {
        negativeClickListener = action
    }

    companion object {
        fun create(
            onPositiveButtonClickListener: (dialog: DialogFragment, tanggal: Calendar, keteranganRencana: String) -> Unit,
            onNegativeButtonClickListener: (dialog: DialogFragment) -> Unit
        ): ConfirmationRencanaDialog {
            return ConfirmationRencanaDialog().apply {
                setOnPositiveClickListener(onPositiveButtonClickListener)
                setOnNegativeButtonClickListener(onNegativeButtonClickListener)
            }
        }
    }

}