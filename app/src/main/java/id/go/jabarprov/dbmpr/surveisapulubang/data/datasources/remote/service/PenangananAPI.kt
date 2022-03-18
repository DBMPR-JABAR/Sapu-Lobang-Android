package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListLubangPenangananRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.PenangananLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.ListLubangPenangananResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PenangananAPI {
    @POST("penanganan-lubang/list")
    suspend fun getListLubangPenanganan(@Body body: ListLubangPenangananRequest): Response<ListLubangPenangananResponse>

    @POST("penanganan-lubang/execute/{id}/{tanggal}")
    suspend fun storePenanganan(
        @Path("id") idLubang: Int,
        @Path("tanggal") tanggal: String,
        @Body penangananLubangRequest: PenangananLubangRequest,
    ): Response<ListLubangPenangananResponse>
}