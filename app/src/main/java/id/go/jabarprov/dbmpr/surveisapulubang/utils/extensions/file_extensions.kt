package id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions

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