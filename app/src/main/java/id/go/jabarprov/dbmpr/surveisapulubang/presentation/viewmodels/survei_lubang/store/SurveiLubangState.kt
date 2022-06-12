package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store

import android.net.Uri
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.*
import java.io.File
import java.util.*

data class SurveiLubangState(
    val tanggal: Calendar = Calendar.getInstance(),
    val idRuasJalan: String = "",
    val ruasJalan: String = "",
    // State
    val tambahLubang: Resource<SurveiLubang> = Resource.Initial(),
    val kurangLubang: Resource<SurveiLubang> = Resource.Initial(),
    val startSurveiLubang: Resource<SurveiLubang> = Resource.Initial(),
    val location: Resource<Unit> = Resource.Initial(),
    val isStarted: Boolean = false,
    // Form Data State
    val lajur: Lajur? = null,
    val ukuran: Ukuran? = null,
    val kedalaman: Kedalaman? = null,
    val isPotential: Boolean = false,
    val kodeLokasi: String = "",
    val lokasiKm: String = "",
    val lokasiM: String = "",
    val keteranganLubang: String? = null,
    val kategoriLubang: KategoriLubang = KategoriLubang.SINGLE,
    val jumlahLubangPerGroup: Int = 0,
    val panjangLubang: Double = 0.0,
    val gambarLubangUri: Uri? = null,
    val gambarLubangFile: File? = null,
) : State