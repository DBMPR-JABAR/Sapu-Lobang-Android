package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.common.data.remote.models.BaseResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ExecuteRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListLubangPerencanaanRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.ListLubangPerencanaanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RencanaAPI {
    @POST("rencana-penanganan-lubang/list")
    suspend fun getListLubang(@Body body: ListLubangPerencanaanRequest): Response<ListLubangPerencanaanResponse>

    @POST("rencana-penanganan-lubang/execute/{id}")
    suspend fun storeRencana(
        @Path("id") id: Int,
        @Body body: ExecuteRequest
    ): Response<ListLubangPerencanaanResponse>

    @GET("sapu-lubang/data-lubang/reject/{id}")
    suspend fun rejectLubang(@Path("id") idLubang: Int): Response<BaseResponse<Unit>>
}