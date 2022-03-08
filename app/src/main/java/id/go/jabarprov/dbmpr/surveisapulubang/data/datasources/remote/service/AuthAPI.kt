package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.common.data.remote.models.BaseResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.LoginRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>
}