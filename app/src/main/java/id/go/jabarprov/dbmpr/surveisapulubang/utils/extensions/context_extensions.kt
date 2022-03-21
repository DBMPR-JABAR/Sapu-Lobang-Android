package id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions

import android.content.Context
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import java.io.File
import java.util.*

fun Context.createPictureCacheFile(fileName: String = CalendarUtils.formatCalendarToString(Calendar.getInstance())): File {
    val cachePictureDir = File(cacheDir, "Pictures")
    if (!cachePictureDir.exists()) {
        cachePictureDir.mkdirs()
    }
    return File(cachePictureDir, "$fileName.jpeg")
}