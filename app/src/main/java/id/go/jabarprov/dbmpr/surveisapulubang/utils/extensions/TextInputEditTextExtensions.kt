package id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.addTextWatcherWithReplacement(onChanged: (Editable?) -> Unit) {
    val editText = this

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            editText.removeTextChangedListener(this)
            onChanged(p0)
            editText.addTextChangedListener(this)
        }
    }

    editText.addTextChangedListener(textWatcher)
}