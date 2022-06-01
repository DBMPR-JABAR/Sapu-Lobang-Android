package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.RekapitulasiRepository
import javax.inject.Inject

class GetRekapPerencanaan @Inject constructor(private val rekapitulasiRepository: RekapitulasiRepository) :
    UseCase<List<Lubang>, None>() {
    override suspend fun run(params: None): Either<Failure, List<Lubang>> {
        return rekapitulasiRepository.getRekapitulasiPerencanaan()
    }
}