package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.PenangananRepository
import java.util.*
import javax.inject.Inject

class GetListLubangPenanganan @Inject constructor(private val penangananRepository: PenangananRepository) :
    UseCase<List<Lubang>, GetListLubangPenanganan.Params>() {

    data class Params(val tanggal: Calendar, val idRuasJalan: String)

    override suspend fun run(params: Params): Either<Failure, List<Lubang>> {
        return penangananRepository.getListLubang(params.tanggal, params.idRuasJalan)
    }

}