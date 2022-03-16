package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.common.data.remote.models.BaseResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.RencanaRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.ListLubangResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RencanaAPI {
    @POST("rencana-penanganan-lubang/list")
    suspend fun getListLubang(@Body body: ListLubangRequest): Response<ListLubangResponse>

    @POST("rencana-penanganan-lubang/store")
    suspend fun storeRencana(@Body body: RencanaRequest): Response<BaseResponse<Unit>>
}