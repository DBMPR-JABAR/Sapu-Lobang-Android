package id.go.jabarprov.dbmpr.surveisapulubang.utils

import android.util.Log

private const val TAG = "GLOBAL_FUNCTIONS"

fun getSapuLubangImageUrl(fileName: String): String {
    return "https://tj.temanjabar.net/map-dashboard/intervention-mage/${fileName}"
}

fun runSafety(action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        Log.e(TAG, "runSafety Exception: ${e.message}", e)
    }
}