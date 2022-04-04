package id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi

interface RekapitulasiRepository {
    suspend fun getRekapitulasi(): Either<Failure, Rekapitulasi>
}