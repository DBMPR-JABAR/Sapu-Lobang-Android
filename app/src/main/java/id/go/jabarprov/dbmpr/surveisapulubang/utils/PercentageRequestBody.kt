package id.go.jabarprov.dbmpr.surveisapulubang.utils

import android.os.Handler
import android.os.Looper
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.getContentType
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class PercentageRequestBody(
    private val file: File,
    private val onProgressUpdate: ((Double) -> Unit)? = null
) : RequestBody() {
    override fun contentType(): MediaType? = file.getContentType().toMediaTypeOrNull()

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploaded = 0L
        fileInputStream.use { fis ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (fis.read(buffer).also { read = it } != -1) {
                handler.post {
                    onProgressUpdate?.invoke((uploaded / length * 100).toDouble())
                }
                uploaded += read
                sink.write(buffer, 0, read)
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 1024
    }
}