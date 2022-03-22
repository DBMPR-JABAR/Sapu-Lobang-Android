package id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import java.util.*

interface PenangananRepository {
    suspend fun storePenanganan(
        idLubang: Int,
        tanggal: Calendar,
        keterangan: String
    ): Either<Failure, List<Lubang>>

    suspend fun getListLubang(
        tanggal: Calendar,
        idRuasJalan: String,
    ): Either<Failure, List<Lubang>>
}