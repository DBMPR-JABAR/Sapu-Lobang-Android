package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.penanganan

import android.util.Log
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service.PenangananAPI
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListLubangPenangananRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.toPercentageMultipart
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.toRequestBody
import java.io.File
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

private const val TAG = "PenangananRemoteDataSou"

class PenangananRemoteDataSourceImpl @Inject constructor(private val penangananAPI: PenangananAPI) :
    PenangananRemoteDataSource {
    override suspend fun storePenanganan(
        idLubang: Int,
        tanggal: Calendar,
        keterangan: String,
        gambarPenanganan: File,
        lat: Double,
        long: Double,
        onProgressUpdate: ((Double) -> Unit)?
    ): List<LubangResponse> {
        try {
            val response = penangananAPI.storePenanganan(
                idLubang = idLubang,
                tanggal = CalendarUtils.formatCalendarToString(tanggal),
                keterangan = keterangan.toRequestBody(),
                gambarPenanganan = gambarPenanganan.toPercentageMultipart(
                    "image_penanganan",
                    onProgressUpdate
                ),
                latitude = lat.toRequestBody(),
                longitude = long.toRequestBody(),
            )
            if (response.code() == 400) {
                throw RemoteDataSourceException("Lokasi Lubang Tidak Sesuai")
            }
            if (!response.isSuccessful) {
                throw RemoteDataSourceException("Gagal Menyimpan Penanganan Lubang")
            }
            return response.body()?.listLubang!! + response.body()?.listFinishedLubang!!
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        }
    }

    override suspend fun getListPenangananLubang(
        idRuasJalan: String,
        tanggal: Calendar
    ): List<LubangResponse> {
        try {
            val request = ListLubangPenangananRequest(
                idRuasJalan,
                CalendarUtils.formatCalendarToString(tanggal)
            )
            val response = penangananAPI.getListLubangPenanganan(request)
            if (!response.isSuccessful) {
                throw RemoteDataSourceException("Gagal Mengambil List Lubang yang Belum Ditangani")
            }
            return response.body()?.listLubang!! + response.body()?.listFinishedLubang!!
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: Exception) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Terjadi Kesalahan Pada Sistem")
        }
    }
}