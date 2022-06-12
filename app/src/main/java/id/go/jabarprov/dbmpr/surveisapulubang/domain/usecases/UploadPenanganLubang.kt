package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.PenangananRepository
import java.io.File
import java.util.*
import javax.inject.Inject

class UploadPenangananLubang @Inject constructor(private val penangananRepository: PenangananRepository) :
    UseCase<List<Lubang>, UploadPenangananLubang.Params>() {

    data class Params(
        val idLubang: Int,
        val tanggal: Calendar,
        val keterangan: String,
        val gambarPenanganan: File,
        val latitude: Double,
        val longitude: Double,
        val onProgressUpdate: ((Double) -> Unit)? = null
    )

    override suspend fun run(params: Params): Either<Failure, List<Lubang>> {
        return penangananRepository.storePenanganan(
            params.idLubang,
            params.tanggal,
            params.keterangan,
            params.gambarPenanganan,
            params.latitude,
            params.longitude,
            params.onProgressUpdate
        )
    }
}