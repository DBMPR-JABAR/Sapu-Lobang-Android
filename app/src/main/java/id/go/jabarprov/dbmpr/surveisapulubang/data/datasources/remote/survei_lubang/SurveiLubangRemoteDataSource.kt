package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.survei_lubang

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.ResultSurveiResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.SurveiLubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Kedalaman
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lajur
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Ukuran
import java.io.File
import java.util.*

interface SurveiLubangRemoteDataSource {
    suspend fun startSurvei(tanggal: Calendar, idRuasJalan: String): SurveiLubangResponse

    suspend fun resultSurvei(tanggal: Calendar, idRuasJalan: String): ResultSurveiResponse

    suspend fun deleteSurveiItem(idLubang: Int)

    suspend fun deleteSurveiPotensiItem(idLubang: Int)

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
        gambarLubang: File,
        keterangan: String?,
        lajur: Lajur,
        ukuran: Ukuran,
        kedalaman: Kedalaman,
        isPotential: Boolean,
        onProgressUpdate: ((Double) -> Unit)? = null
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