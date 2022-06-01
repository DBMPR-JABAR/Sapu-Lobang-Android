package id.go.jabarprov.dbmpr.surveisapulubang.data.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toError
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toSuccess
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.RemoteDataSourceFailure
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rekapitulasi.RekapitulasiRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.mapper.LubangDataMapper
import id.go.jabarprov.dbmpr.surveisapulubang.data.mapper.RekapitulasiDataMapper
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.RekapitulasiRepository
import javax.inject.Inject

class RekapitulasiRepositoryImpl @Inject constructor(private val rekapitulasiRemoteDataSource: RekapitulasiRemoteDataSource) :
    RekapitulasiRepository {
    override suspend fun getRekapitulasi(): Either<Failure, Rekapitulasi> {
        return try {
            val response = rekapitulasiRemoteDataSource.getRekapitulasi()
            RekapitulasiDataMapper.convertRekapitulasiResponseToEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun getRekapitulasiLubang(): Either<Failure, List<Lubang>> {
        return try {
            val response = rekapitulasiRemoteDataSource.getRekapitulasiLubang()
            LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun getRekapitulasiPotensi(): Either<Failure, List<Lubang>> {
        return try {
            val response = rekapitulasiRemoteDataSource.getRekapitulasiPotensi()
            LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun getRekapitulasiPerencanaan(): Either<Failure, List<Lubang>> {
        return try {
            val response = rekapitulasiRemoteDataSource.getRekapitulasiPerencanaan()
            LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun getRekapitulasiPenanganan(): Either<Failure, List<Lubang>> {
        return try {
            val response = rekapitulasiRemoteDataSource.getRekapitulasiPenanganan()
            LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }


}