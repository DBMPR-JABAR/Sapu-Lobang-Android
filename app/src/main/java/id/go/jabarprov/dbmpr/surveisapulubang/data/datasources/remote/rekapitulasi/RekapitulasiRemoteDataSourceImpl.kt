package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rekapitulasi

import android.util.Log
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service.RekapAPI
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.RekapitulasiResponse
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

private const val TAG = "RekapitulasiRemoteDataS"

class RekapitulasiRemoteDataSourceImpl @Inject constructor(private val rekapAPI: RekapAPI) :
    RekapitulasiRemoteDataSource {
    override suspend fun getRekapitulasi(): RekapitulasiResponse {
        try {
            val response = rekapAPI.getRekapitulasi()
            if (response.isSuccessful) {
                return response.body()?.data!!
            } else {
                Log.d(TAG, "startSurvei: Gagal Mengambil Data Hasil Survei")
                throw RemoteDataSourceException("Gagal Mengambil Data Hasil Survei")
            }
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

    override suspend fun getRekapitulasiLubang(): List<LubangResponse> {
        try {
            val response = rekapAPI.getRekapitulasiLubang()
            if (response.isSuccessful) {
                return response.body()?.data!!
            } else {
                Log.d(TAG, "startSurvei: Gagal Mengambil Data Rekap Lubang")
                throw RemoteDataSourceException("Gagal Mengambil Data Rekap Lubang")
            }
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

    override suspend fun getRekapitulasiPotensi(): List<LubangResponse> {
        try {
            val response = rekapAPI.getRekapitulasiPotensi()
            if (response.isSuccessful) {
                return response.body()?.data!!
            } else {
                Log.d(TAG, "startSurvei: Gagal Mengambil Data Rekap Potensi")
                throw RemoteDataSourceException("Gagal Mengambil Data Rekap Potensi")
            }
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

    override suspend fun getRekapitulasiPerencanaan(): List<LubangResponse> {
        try {
            val response = rekapAPI.getRekapitulasiPerencanaan()
            if (response.isSuccessful) {
                return response.body()?.data!!
            } else {
                Log.d(TAG, "startSurvei: Gagal Mengambil Data Rekap Perencanaan")
                throw RemoteDataSourceException("Gagal Mengambil Data Rekap Perencanaan")
            }
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

    override suspend fun getRekapitulasiPenanganan(): List<LubangResponse> {
        try {
            val response = rekapAPI.getRekapitulasiPenanganan()
            if (response.isSuccessful) {
                return response.body()?.data!!
            } else {
                Log.d(TAG, "startSurvei: Gagal Mengambil Data Rekap Penanganan")
                throw RemoteDataSourceException("Gagal Mengambil Data Rekap Penanganan")
            }
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