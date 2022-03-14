package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.survei_lubang

import android.util.Log
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service.SurveiLubangAPI
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.StartSurveiLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.SurveiLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.SurveiLubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

private const val TAG = "SurveiLubangRemoteDataS"

class SurveiLubangRemoteDataSourceImpl @Inject constructor(private val surveiLubangAPI: SurveiLubangAPI) :
    SurveiLubangRemoteDataSource {
    override suspend fun startSurvei(tanggal: Calendar, idRuasJalan: String): SurveiLubangResponse {
        try {
            val request =
                StartSurveiLubangRequest(CalendarUtils.formatCalendarToString(tanggal), idRuasJalan)
            val response = surveiLubangAPI.startSurvei(request)
            if (response.isSuccessful) {
                return response.body()?.data!!
            } else {
                Log.d(TAG, "startSurvei: Gagal Memulai Survei")
                throw RemoteDataSourceException("Gagal Memulai Survei")
            }
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: Exception) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Terjadi Kesalahan Pada Sistem")
        }
    }

    override suspend fun tambahLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double?,
        long: Double?
    ): SurveiLubangResponse {
        try {
            val request =
                SurveiLubangRequest(
                    CalendarUtils.formatCalendarToString(tanggal),
                    idRuasJalan,
                    kodeLokasi,
                    lokasiKm,
                    lokasiM,
                    lat,
                    long
                )
            val response = surveiLubangAPI.tambahLubang(request)
            if (response.isSuccessful) {
                return response.body()?.data!!
            } else {
                Log.d(TAG, "startSurvei: Gagal Menambah Lubang Survei")
                throw RemoteDataSourceException("Gagal Menambah Lubang Survei")
            }
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: Exception) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Terjadi Kesalahan Pada Sistem")
        }
    }

    override suspend fun kurangLubang(
        tanggal: Calendar,
        idRuasJalan: String,
        kodeLokasi: String,
        lokasiKm: String,
        lokasiM: String,
        lat: Double?,
        long: Double?
    ): SurveiLubangResponse {
        try {
            val request =
                SurveiLubangRequest(
                    CalendarUtils.formatCalendarToString(tanggal),
                    idRuasJalan,
                    kodeLokasi,
                    lokasiKm,
                    lokasiM,
                    lat,
                    long
                )
            val response = surveiLubangAPI.kurangLubang(request)
            if (response.isSuccessful) {
                return response.body()?.data!!
            } else {
                Log.d(TAG, "startSurvei: Gagal Mengurangi Lubang Survei")
                throw RemoteDataSourceException("Gagal Mengurangi Lubang Survei")
            }
        } catch (e: Exception) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: Exception) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Terjadi Kesalahan Pada Sistem")
        }
    }

}