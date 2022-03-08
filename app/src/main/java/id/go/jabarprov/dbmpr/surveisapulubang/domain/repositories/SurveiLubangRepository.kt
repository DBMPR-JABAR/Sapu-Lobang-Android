package id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.SurveiLubang
import java.util.*

interface SurveiLubangRepository {
    suspend fun startSurvei(tanggal: Calendar, idRuasJalan: String): Either<Failure, SurveiLubang>

    suspend fun tambahLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double? = null,
        long: Double? = null
    ): Either<Failure, SurveiLubang>

    suspend fun kurangLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double? = null,
        long: Double? = null
    ): Either<Failure, SurveiLubang>
}