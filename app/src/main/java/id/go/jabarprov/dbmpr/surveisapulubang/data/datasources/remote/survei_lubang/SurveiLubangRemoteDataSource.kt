package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.survei_lubang

import android.net.Uri
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.SurveiLubangResponse
import java.io.File
import java.util.*

interface SurveiLubangRemoteDataSource {
    suspend fun startSurvei(tanggal: Calendar, idRuasJalan: String): SurveiLubangResponse

    suspend fun tambahLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double,
        long: Double,
        panjangLubang: Double,
        jumlahLubangPerGroup: Int? = null,
        kategoriLubang: String,
        gambarLubang: File
    ): SurveiLubangResponse

    suspend fun kurangLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double,
        long: Double
    ): SurveiLubangResponse
}