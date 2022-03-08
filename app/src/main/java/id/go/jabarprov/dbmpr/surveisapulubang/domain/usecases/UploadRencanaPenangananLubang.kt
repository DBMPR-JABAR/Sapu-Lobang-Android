package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.RencanaRepository
import java.util.*
import javax.inject.Inject

class UploadRencanaPenangananLubang @Inject constructor(private val rencanaRepository: RencanaRepository) :
    UseCase<None, UploadRencanaPenangananLubang.Params>() {
    data class Params(
        val tanggal: Calendar,
        val idRuasJalan: String,
        val jumlah: Int
    )

    override suspend fun run(params: Params): Either<Failure, None> {
        return rencanaRepository.storeRencana(
            params.tanggal,
            params.idRuasJalan,
            params.jumlah
        )
    }
}