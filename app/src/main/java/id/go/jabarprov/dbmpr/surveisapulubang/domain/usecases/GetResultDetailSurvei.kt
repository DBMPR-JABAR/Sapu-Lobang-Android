package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.SurveiLubangRepository
import java.util.*
import javax.inject.Inject

class GetResultDetailSurvei @Inject constructor(private val surveiLubangRepository: SurveiLubangRepository) :
    UseCase<List<Lubang>, GetResultDetailSurvei.Params>() {

    data class Params(val tanggal: Calendar, val idRuasJalan: String)

    override suspend fun run(params: Params): Either<Failure, List<Lubang>> {
        return surveiLubangRepository.resultSurvei(params.tanggal, params.idRuasJalan)
    }
}