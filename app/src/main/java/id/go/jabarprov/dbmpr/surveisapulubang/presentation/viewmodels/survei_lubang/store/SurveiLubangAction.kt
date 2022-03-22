package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store

import android.net.Uri
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action
import java.io.File
import java.util.*

sealed class SurveiLubangAction : Action {
    data class StartSurveiAction(val tanggal: Calendar, val idRuasJalan: String) :
        SurveiLubangAction()

    data class TambahLubangAction(val lat: Double, val long: Double) :
        SurveiLubangAction()

    data class KurangLubangAction(val lat: Double, val long: Double) :
        SurveiLubangAction()

    data class UpdateRuasJalan(val ruasJalan: String) : SurveiLubangAction()

    data class UpdateTanggal(val timeInMillis: Long) : SurveiLubangAction()

    data class UpdateKodeLokasi(val kodeLokasi: String) : SurveiLubangAction()

    data class UpdateLokasiKm(val lokasiKm: String) : SurveiLubangAction()

    data class UpdateLokasiM(val lokasiM: String) : SurveiLubangAction()

    data class UpdateKategoriLubang(val kategoriLubang: KategoriLubang) : SurveiLubangAction()

    data class UpdatePanjangLubang(val panjangLubang: Double) : SurveiLubangAction()

    data class UpdateJumlahLubangPerGroup(val jumlahLubangPerGroup: Int) : SurveiLubangAction()

    data class UpdateFotoLubang(val gambarLubang: Uri, val gambarLubangFile: File) :
        SurveiLubangAction()

}