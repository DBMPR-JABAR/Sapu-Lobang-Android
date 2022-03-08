package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.penanganan

import android.util.Log
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service.PenangananAPI
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListUnhandledLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.PenangananRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.UnhandledLubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

private const val TAG = "PenangananRemoteDataSou"

class PenangananRemoteDataSourceImpl @Inject constructor(private val penangananAPI: PenangananAPI) :
    PenangananRemoteDataSource {
    override suspend fun storePenanganan(
        tanggal: Calendar,
        idRuasJalan: String,
        jumlah: Int
    ) {
        try {
            val request = PenangananRequest(
                tanggal = CalendarUtils.formatCalendarToString(tanggal),
                idRuasJalan = idRuasJalan,
                jumlah = jumlah
            )
            val response = penangananAPI.storePenanganan(request)
            if (!response.isSuccessful) {
                throw RemoteDataSourceException("Gagal Menyimpan Penanganan Lubang")
            }
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        }
    }

    override suspend fun getListUnhandledLubang(
        idRuasJalan: String,
        tanggal: Calendar
    ): List<UnhandledLubangResponse> {
        try {
            val request = ListUnhandledLubangRequest(
                idRuasJalan,
                CalendarUtils.formatCalendarToString(tanggal)
            )
            val response = penangananAPI.getListUnhandledLubang(request)
            if (!response.isSuccessful) {
                throw RemoteDataSourceException("Gagal Mengambil List Lubang yang Belum Ditangani")
            }
            return response.body()?.data!!
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        }
    }
}