package id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.*
import java.io.File
import java.util.*

interface SurveiLubangRepository {
    suspend fun startSurvei(tanggal: Calendar, idRuasJalan: String): Either<Failure, SurveiLubang>

    suspend fun resultSurvei(tanggal: Calendar, idRuasJalan: String): Either<Failure, ResultSurvei>

    suspend fun deleteSurveiItem(idLubang: Int): Either<Failure, Unit>

    suspend fun deleteSurveiPotensiItem(idLubang: Int): Either<Failure, Unit>

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
    ): Either<Failure, SurveiLubang>

    suspend fun kurangLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double,
        long: Double
    ): Either<Failure, SurveiLubang>
}