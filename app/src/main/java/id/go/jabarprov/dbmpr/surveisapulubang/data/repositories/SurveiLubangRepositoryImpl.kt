package id.go.jabarprov.dbmpr.surveisapulubang.data.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toError
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toSuccess
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.RemoteDataSourceFailure
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.survei_lubang.SurveiLubangRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.mapper.LubangDataMapper
import id.go.jabarprov.dbmpr.surveisapulubang.data.mapper.SurveiLubangDataMapper
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.SurveiLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.SurveiLubangRepository
import java.io.File
import java.util.*
import javax.inject.Inject

class SurveiLubangRepositoryImpl @Inject constructor(private val surveiLubangRemoteDataSource: SurveiLubangRemoteDataSource) :
    SurveiLubangRepository {

    override suspend fun startSurvei(
        tanggal: Calendar,
        idRuasJalan: String
    ): Either<Failure, SurveiLubang> {
        return try {
            val response = surveiLubangRemoteDataSource.startSurvei(tanggal, idRuasJalan)
            SurveiLubangDataMapper.convertSurveiLubangDataResponseToEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun resultSurvei(
        tanggal: Calendar,
        idRuasJalan: String
    ): Either<Failure, List<Lubang>> {
        return try {
            val response = surveiLubangRemoteDataSource.resultSurvei(tanggal, idRuasJalan)
            LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun tambahLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double,
        long: Double,
        panjangLubang: Double,
        jumlahLubangPerGroup: Int?,
        kategoriLubang: String,
        gambarLubang: File
    ): Either<Failure, SurveiLubang> {
        return try {
            val response =
                surveiLubangRemoteDataSource.tambahLubang(
                    tanggal,
                    idRuasJalan,
                    kodeLokasi,
                    lokasiKm,
                    lokasiM,
                    lat,
                    long,
                    panjangLubang,
                    jumlahLubangPerGroup,
                    kategoriLubang,
                    gambarLubang
                )
            SurveiLubangDataMapper.convertSurveiLubangDataResponseToEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun kurangLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double,
        long: Double
    ): Either<Failure, SurveiLubang> {
        return try {
            val response =
                surveiLubangRemoteDataSource.kurangLubang(
                    tanggal,
                    idRuasJalan,
                    kodeLokasi,
                    lokasiKm,
                    lokasiM,
                    lat,
                    long
                )
            SurveiLubangDataMapper.convertSurveiLubangDataResponseToEntity(response).toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }
}