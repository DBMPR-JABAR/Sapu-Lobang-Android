package id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions

import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun <T> T?.toRequestBody(): RequestBody {
    return toString().toRequestBody()
}

fun <T> T?.getValueOrElse(defaultValue: T): T {
    return when {
        this == null -> defaultValue
        else -> {
            this
        }
    }
}