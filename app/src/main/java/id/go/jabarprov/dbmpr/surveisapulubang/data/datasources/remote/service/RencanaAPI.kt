package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ExecuteRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.ListLubangPerencanaanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface RencanaAPI {
    @POST("rencana-penanganan-lubang/list")
    suspend fun getListLubang(@Body body: ListLubangRequest): Response<ListLubangPerencanaanResponse>

//    @POST("rencana-penanganan-lubang/store")
//    suspend fun storeRencana(@Body body: RencanaRequest): Response<BaseResponse<Unit>>

    @POST("rencana-penanganan-lubang/execute/{id}")
    suspend fun storeRencana(
        @Path("id") id: Int,
        @Body body: ExecuteRequest
    ): Response<ListLubangPerencanaanResponse>
}