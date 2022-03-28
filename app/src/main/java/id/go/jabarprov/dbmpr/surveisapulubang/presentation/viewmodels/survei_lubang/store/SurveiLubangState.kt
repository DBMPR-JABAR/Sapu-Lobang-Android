package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store

import android.net.Uri
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.KategoriLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Kedalaman
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lajur
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Ukuran
import java.io.File
import java.util.*

data class SurveiLubangState(
    val tanggal: Calendar = Calendar.getInstance(),
    val idRuasJalan: String = "",
    val isFailed: Boolean = false,
    val errorMessage: String = "",
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val kategoriLubang: KategoriLubang = KategoriLubang.SINGLE,
    val jumlahLubangTotal: Int = 0,
    val panjangLubangTotal: Double = 0.0,
    val jumlahLubangPerGroup: Int = 0,
    val panjangLubang: Double = 0.0,
    val gambarLubangUri: Uri? = null,
    val gambarLubangFile: File? = null,
    val isStarted: Boolean = false,
    val ruasJalan: String = "",
    val kodeLokasi: String = "",
    val lokasiKm: String = "",
    val lokasiM: String = "",
    val keteranganLubang: String? = null,
    val isResetting: Boolean = false,
    val lajur: Lajur? = null,
    val ukuran: Ukuran? = null,
    val kedalaman: Kedalaman? = null,
) : State