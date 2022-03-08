package id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.UnhandledLubang
import java.util.*

interface PenangananRepository {
    suspend fun storePenanganan(
        tanggal: Calendar,
        idRuasJalan: String,
        jumlah: Int
    ): Either<Failure, None>

    suspend fun getListUnhandledLubang(
        tanggal: Calendar,
        idRuasJalan: String,
    ): Either<Failure, List<UnhandledLubang>>

    suspend fun resolveUnhandledLubang(
        idUnhandledLubang: Int,
        tanggal: Calendar
    ): Either<Failure, List<UnhandledLubang>>
}