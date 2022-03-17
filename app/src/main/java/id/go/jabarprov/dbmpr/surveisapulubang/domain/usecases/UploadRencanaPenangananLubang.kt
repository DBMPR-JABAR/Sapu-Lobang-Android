package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.RencanaRepository
import java.util.*
import javax.inject.Inject

class UploadRencanaPenangananLubang @Inject constructor(private val rencanaRepository: RencanaRepository) :
    UseCase<List<Lubang>, UploadRencanaPenangananLubang.Params>() {
    data class Params(
        val idLubang: Int,
        val tanggal: Calendar,
        val keterangan: String
    )

    override suspend fun run(params: Params): Either<Failure, List<Lubang>> {
        return rencanaRepository.storeRencana(
            params.idLubang,
            params.tanggal,
            params.keterangan
        )
    }
}