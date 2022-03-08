package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.common.data.remote.models.BaseResponse
import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListUnhandledLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.PenangananRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.UnhandledLubangResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PenangananAPI {
    @POST("penanganan-lubang/store")
    suspend fun storePenanganan(@Body body: PenangananRequest): Response<BaseResponse<None>>

    @POST("penanganan-lubang/list")
    suspend fun getListUnhandledLubang(@Body body: ListUnhandledLubangRequest): Response<BaseResponse<List<UnhandledLubangResponse>>>
}