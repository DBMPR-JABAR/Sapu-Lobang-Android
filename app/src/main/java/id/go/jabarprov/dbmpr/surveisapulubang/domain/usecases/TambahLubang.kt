package id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.usecase.UseCase
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.SurveiLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.SurveiLubangRepository
import java.io.File
import java.util.*
import javax.inject.Inject

class TambahLubang @Inject constructor(private val surveiLubangRepository: SurveiLubangRepository) :
    UseCase<SurveiLubang, TambahLubang.Params>() {

    data class Params(
        val tanggal: Calendar,
        val idRuasJalan: String,
        val kodeLokasi: String,
        val lokasiKm: String,
        val lokasiM: String,
        val lat: Double,
        val long: Double,
        val panjangLubang: Double,
        val jumlahLubangPerGroup: Int? = null,
        val kategoriLubang: String,
        val gambarLubang: File
    )

    override suspend fun run(params: Params): Either<Failure, SurveiLubang> {
        return surveiLubangRepository.tambahLubang(
            params.tanggal,
            params.idRuasJalan,
            params.kodeLokasi,
            params.lokasiKm,
            params.lokasiM,
            params.lat,
            params.long,
            params.panjangLubang,
            params.jumlahLubangPerGroup,
            params.kategoriLubang,
            params.gambarLubang
        )
    }

}