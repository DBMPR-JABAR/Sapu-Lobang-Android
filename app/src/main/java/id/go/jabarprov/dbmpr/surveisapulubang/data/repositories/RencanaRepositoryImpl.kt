package id.go.jabarprov.dbmpr.surveisapulubang.data.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toError
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toSuccess
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.RemoteDataSourceFailure
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rencana.RencanaRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.mapper.LubangDataMapper
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.RencanaRepository
import java.util.*
import javax.inject.Inject

class RencanaRepositoryImpl @Inject constructor(private val rencanaRemoteDataSource: RencanaRemoteDataSource) :
    RencanaRepository {
    override suspend fun getListLubang(
        tanggal: Calendar,
        idRuasJalan: String
    ): Either<Failure, List<Lubang>> {
        return try {
            val response = rencanaRemoteDataSource.getListLubang(tanggal, idRuasJalan)
            LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun storeRencana(
        idLubang: Int,
        tanggal: Calendar,
        keterangan: String
    ): Either<Failure, List<Lubang>> {
        return try {
            val response = rencanaRemoteDataSource.storeRencana(idLubang, tanggal, keterangan)
            LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }
}