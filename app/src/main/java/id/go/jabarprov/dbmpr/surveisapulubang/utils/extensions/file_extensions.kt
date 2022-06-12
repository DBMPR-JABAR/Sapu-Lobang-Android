package id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions

import id.go.jabarprov.dbmpr.surveisapulubang.utils.PercentageRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.net.URLConnection

fun File.getContentType(): String {
    return URLConnection.getFileNameMap().getContentTypeFor(name)
}

fun File.toMultipart(fieldName: String): MultipartBody.Part {
    val requestBody = asRequestBody(getContentType().toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(fieldName, name, requestBody)
}

fun File.toPercentageMultipart(
    fieldName: String,
    onProgressUpdate: ((Double) -> Unit)? = null
): MultipartBody.Part {
    val requestBody = PercentageRequestBody(this, onProgressUpdate)
    return MultipartBody.Part.createFormData(fieldName, name, requestBody)
}