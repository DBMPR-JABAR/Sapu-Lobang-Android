package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.survei_lubang

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.SurveiLubangResponse
import java.util.*

interface SurveiLubangRemoteDataSource {
    suspend fun startSurvei(tanggal: Calendar, idRuasJalan: String): SurveiLubangResponse

    suspend fun tambahLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double? = null,
        long: Double? = null
    ): SurveiLubangResponse

    suspend fun kurangLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double? = null,
        long: Double? = null
    ): SurveiLubangResponse
}