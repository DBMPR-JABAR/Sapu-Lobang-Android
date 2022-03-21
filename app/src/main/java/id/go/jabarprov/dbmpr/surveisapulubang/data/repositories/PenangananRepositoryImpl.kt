package id.go.jabarprov.dbmpr.surveisapulubang.data.repositories

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toError
import id.go.jabarprov.dbmpr.surveisapulubang.core.extensions.toSuccess
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.Failure
import id.go.jabarprov.dbmpr.surveisapulubang.core.failures.RemoteDataSourceFailure
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.penanganan.PenangananRemoteDataSource
import id.go.jabarprov.dbmpr.surveisapulubang.data.mapper.UnhandledLubangMapper
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.UnhandledLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.repositories.PenangananRepository
import java.util.*
import javax.inject.Inject

class PenangananRepositoryImpl @Inject constructor(private val penangananRemoteDataSource: PenangananRemoteDataSource) :
    PenangananRepository {
    override suspend fun storePenanganan(
        tanggal: Calendar,
        idRuasJalan: String,
        jumlah: Int
    ): Either<Failure, None> {
        return try {
//            penangananRemoteDataSource.storePenanganan(tanggal, idRuasJalan, jumlah)
            None.toSuccess()
        } catch (e: RemoteDataSourceException) {
            RemoteDataSourceFailure(e.message!!).toError()
        }
    }

    override suspend fun getListUnhandledLubang(
        tanggal: Calendar,
        idRuasJalan: String
    ): Either<Failure, List<UnhandledLubang>> {
//        return try {
//            val response = penangananRemoteDataSource.getListPenangananLubang(idRuasJalan, tanggal)
//            UnhandledLubangMapper.convertListOfUnhandledLubangResponseToListOfEntity(response)
//                .toSuccess()
//        } catch (e: RemoteDataSourceException) {
//            RemoteDataSourceFailure(e.message!!).toError()
//        }
        TODO()
    }

    override suspend fun resolveUnhandledLubang(
        idUnhandledLubang: Int,
        tanggal: Calendar,
        keterangan: String
    ): Either<Failure, List<UnhandledLubang>> {
//        return try {
//            val response =
//                penangananRemoteDataSource.resolveUnhandledLubang(
//                    idUnhandledLubang,
//                    tanggal,
//                    keterangan
//                )
//            UnhandledLubangMapper.convertListOfUnhandledLubangResponseToListOfEntity(response)
//                .toSuccess()
//        } catch (e: RemoteDataSourceException) {
//            RemoteDataSourceFailure(e.message!!).toError()
//        }
        TODO()
    }
}