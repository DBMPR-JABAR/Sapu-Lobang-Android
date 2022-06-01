package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.common.data.remote.models.BaseResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.RekapitulasiResponse
import retrofit2.Response
import retrofit2.http.GET

interface RekapAPI {
    @GET("sapu-lubang/rekapitulasi")
    suspend fun getRekapitulasi(): Response<BaseResponse<RekapitulasiResponse>>

    @GET("sapu-lubang/list/lubang/belum_ditangani")
    suspend fun getRekapitulasiLubang(): Response<BaseResponse<List<LubangResponse>>>

    @GET("sapu-lubang/list/potensi/show")
    suspend fun getRekapitulasiPotensi(): Response<BaseResponse<List<LubangResponse>>>

    @GET("sapu-lubang/list/lubang/dalam_perencanaan")
    suspend fun getRekapitulasiPerencanaan(): Response<BaseResponse<List<LubangResponse>>>

    @GET("sapu-lubang/list/lubang/sudah_ditangani")
    suspend fun getRekapitulasiPenanganan(): Response<BaseResponse<List<LubangResponse>>>
}