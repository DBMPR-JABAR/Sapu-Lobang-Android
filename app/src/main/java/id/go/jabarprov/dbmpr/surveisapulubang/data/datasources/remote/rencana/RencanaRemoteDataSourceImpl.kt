package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rencana

import android.util.Log
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service.RencanaAPI
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.RencanaRequest
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
                ListLubangRequest(idRuasJalan, CalendarUtils.formatCalendarToString(tanggal))
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
        tanggal: Calendar,
        idRuasJalan: String,
        jumlah: Int
    ) {
        try {
            val request = RencanaRequest(
                tanggal = CalendarUtils.formatCalendarToString(tanggal),
                idRuasJalan = idRuasJalan,
                jumlah = jumlah
            )
            val response = rencanaAPI.storeRencana(request)
            if (!response.isSuccessful) {
                throw RemoteDataSourceException("Gagal Menyimpan Rencana Penanganan Lubang")
            }
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: Exception) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Terjadi Kesalahan Pada Sistem")
        }
    }
}