package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rencana

import android.util.Log
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service.RencanaAPI
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ExecuteRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListLubangPerencanaanRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

private const val TAG = "RencanaRemoteDataSource"

class RencanaRemoteDataSourceImpl @Inject constructor(private val rencanaAPI: RencanaAPI) :
    RencanaRemoteDataSource {
    override suspend fun getListLubang(
        tanggal: Calendar,
        idRuasJalan: String
    ): List<LubangResponse> {
        try {
            val request =
                ListLubangPerencanaanRequest(idRuasJalan, CalendarUtils.formatCalendarToString(tanggal))
            val response = rencanaAPI.getListLubang(request)
            if (!response.isSuccessful) {
                throw RemoteDataSourceException("Gagal Mengambil List Lubang Hasil Survei Penanganan Lubang")
            }
            return response.body()?.listLubang!! + response.body()?.listScheduledLubang!!
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: Exception) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Terjadi Kesalahan Pada Sistem")
        }
    }

    override suspend fun storeRencana(
        idLubang: Int,
        tanggal: Calendar,
        keterangan: String
    ): List<LubangResponse> {
        try {
            val request = ExecuteRequest(
                tanggal = CalendarUtils.formatCalendarToString(tanggal),
                keterangan = keterangan
            )
            val response = rencanaAPI.storeRencana(idLubang, request)
            if (!response.isSuccessful) {
                throw RemoteDataSourceException("Gagal Menyimpan Rencana Penanganan Lubang")
            }
            return response.body()?.listLubang!! + response.body()?.listScheduledLubang!!
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: Exception) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Terjadi Kesalahan Pada Sistem")
        }
    }
}