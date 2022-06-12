package id.go.jabarprov.dbmpr.surveisapulubang.data.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toError
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toSuccess
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.RemoteDataSourceFailure
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.penanganan.PenangananRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.mapper.LubangDataMapper
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.PenangananRepository
import java.io.File
import java.util.*
import javax.inject.Inject

class PenangananRepositoryImpl @Inject constructor(private val penangananRemoteDataSource: PenangananRemoteDataSource) :
    PenangananRepository {
    override suspend fun storePenanganan(
        idLubang: Int,
        tanggal: Calendar,
        keterangan: String,
        gambarPenanganan: File,
        lat: Double,
        long: Double,
        onProgressUpdate: ((Double) -> Unit)?
    ): Either<Failure, List<Lubang>> {
        return try {
            val response = penangananRemoteDataSource.storePenanganan(
                idLubang,
                tanggal,
                keterangan,
                gambarPenanganan,
                lat,
                long,
                onProgressUpdate
            )
            LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun getListLubang(
        tanggal: Calendar,
        idRuasJalan: String
    ): Either<Failure, List<Lubang>> {
        return try {
            val response = penangananRemoteDataSource.getListPenangananLubang(idRuasJalan, tanggal)
            LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(response)
                .toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }
}