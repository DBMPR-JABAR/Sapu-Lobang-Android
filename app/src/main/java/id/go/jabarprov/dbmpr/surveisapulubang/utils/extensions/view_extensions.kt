package id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView

fun ImageView.setImageUri(uri: Uri) {
    context.contentResolver.openFileDescriptor(uri, "r")
        .use { parcelFileDescriptor ->
            parcelFileDescriptor?.fileDescriptor?.let { fileDescriptor ->
                val bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                setImageBitmap(bitmap)
            }
        }
}