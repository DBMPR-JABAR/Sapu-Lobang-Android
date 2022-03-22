package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.SurveiLubangRepository
import javax.inject.Inject

class DeleteSurveiItem @Inject constructor(private val surveiLubangRepository: SurveiLubangRepository) :
    UseCase<Unit, Int>() {
    override suspend fun run(params: Int): Either<Failure, Unit> {
        return surveiLubangRepository.deleteSurveiItem(params)
    }
}