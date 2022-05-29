package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.auth

import android.util.Log
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.RemoteDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service.AuthAPI
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.LoginRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LoginResponse
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

private const val TAG = "AuthRemoteDataSourceImp"

class AuthRemoteDataSourceImpl @Inject constructor(private val authAPI: AuthAPI) :
    AuthRemoteDataSource {
    override suspend fun login(username: String, password: String): LoginResponse {
        try {
            val request = LoginRequest(username, password)
            val response = authAPI.login(request)
            if (response.isSuccessful) {
                return response.body()?.data!!
            } else {
                Log.d(TAG, "login: ERROR LOGIN ${response.errorBody().toString()}")
                if (response.code() == 400) {
                    throw RemoteDataSourceException("Username/NIP/NIK atau password salah")
                } else {
                    throw RemoteDataSourceException("Tidak Dapat Login Pada Aplikasi")
                }
            }
        } catch (e: UnknownHostException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "login: ERROR LOGIN $e")
            throw RemoteDataSourceException("Tidak Dapat Menghubungi Server")
        }
    }
}