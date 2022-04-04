package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.RekapitulasiRepository
import javax.inject.Inject

class GetRekapitulasi @Inject constructor(private val rekapitulasiRepository: RekapitulasiRepository) :
    UseCase<Rekapitulasi, None>() {
    override suspend fun run(params: None): Either<Failure, Rekapitulasi> {
        return rekapitulasiRepository.getRekapitulasi()
    }
}