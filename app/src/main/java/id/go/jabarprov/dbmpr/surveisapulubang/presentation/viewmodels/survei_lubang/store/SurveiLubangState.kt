package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store

import android.graphics.Bitmap
import android.net.Uri
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import java.util.*

data class SurveiLubangState(
    val tanggal: Calendar = Calendar.getInstance(),
    val idRuasJalan: String = "",
    val isFailed: Boolean = false,
    val errorMessage: String = "",
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val kategoriLubang: KategoriLubang = KategoriLubang.SINGLE,
    val jumlahLubang: Int = 0,
    val jumlahLubangPerGroup: Int = 0,
    val panjangLubang: Int = 0,
    val gambarLubang: Uri? = null,
    val isStarted: Boolean = false,
    val ruasJalan: String = "",
    val kodeLokasi: String = "",
    val lokasiKm: String = "",
    val lokasiM: String = ""
) : State

enum class KategoriLubang {
    SINGLE,
    GROUP
}