package id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import java.io.File
import java.util.*

interface PenangananRepository {
    suspend fun storePenanganan(
        idLubang: Int,
        tanggal: Calendar,
        keterangan: String,
        gambarPenanganan: File,
        lat: Double,
        long: Double,
        onProgressUpdate: ((Double) -> Unit)? = null
    ): Either<Failure, List<Lubang>>

    suspend fun getListLubang(
        tanggal: Calendar,
        idRuasJalan: String,
    ): Either<Failure, List<Lubang>>
}