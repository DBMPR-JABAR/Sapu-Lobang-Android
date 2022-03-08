package id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import java.util.*

interface PenangananRepository {
    suspend fun storePenanganan(
        tanggal: Calendar,
        idRuasJalan: String,
        jumlah: Int
    ): Either<Failure, None>
}