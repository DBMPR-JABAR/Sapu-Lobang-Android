package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.common.data.remote.models.BaseResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.RekapitulasiResponse
import retrofit2.Response
import retrofit2.http.GET

interface RekapAPI {
    @GET("sapu-lubang/rekapitulasi")
    suspend fun getRekapitulasi(): Response<BaseResponse<RekapitulasiResponse>>
}