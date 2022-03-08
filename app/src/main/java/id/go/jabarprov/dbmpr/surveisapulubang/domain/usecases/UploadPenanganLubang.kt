package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.PenangananRepository
import java.util.*
import javax.inject.Inject

class UploadPenangananLubang @Inject constructor(private val penangananRepository: PenangananRepository) :
    UseCase<None, UploadPenangananLubang.Params>() {
    data class Params(
        val tanggal: Calendar,
        val idRuasJalan: String,
        val jumlah: Int
    )

    override suspend fun run(params: Params): Either<Failure, None> {
        return penangananRepository.storePenanganan(
            params.tanggal,
            params.idRuasJalan,
            params.jumlah
        )
    }
}