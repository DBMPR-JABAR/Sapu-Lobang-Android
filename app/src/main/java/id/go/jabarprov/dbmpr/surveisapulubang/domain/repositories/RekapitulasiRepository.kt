package id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi

interface RekapitulasiRepository {
    suspend fun getRekapitulasi(): Either<Failure, Rekapitulasi>
    suspend fun getRekapitulasiLubang(): Either<Failure, List<Lubang>>
    suspend fun getRekapitulasiPotensi(): Either<Failure, List<Lubang>>
    suspend fun getRekapitulasiPerencanaan(): Either<Failure, List<Lubang>>
    suspend fun getRekapitulasiPenanganan(): Either<Failure, List<Lubang>>
}