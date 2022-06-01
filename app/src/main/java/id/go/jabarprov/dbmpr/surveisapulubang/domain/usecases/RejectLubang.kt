package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.RencanaRepository
import javax.inject.Inject

class RejectLubang @Inject constructor(private val rencanaRepository: RencanaRepository) :
    UseCase<Unit, RejectLubang.Params>() {

    data class Params(
        val idLubang: Int
    )

    override suspend fun run(params: Params): Either<Failure, Unit> {
        return rencanaRepository.rejectLubang(params.idLubang)
    }
}