package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.ListLubangPenangananRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.ListLubangPenangananResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PenangananAPI {
    @POST("penanganan-lubang/list")
    suspend fun getListLubangPenanganan(@Body body: ListLubangPenangananRequest): Response<ListLubangPenangananResponse>

    @POST("penanganan-lubang/execute/{id}/{tanggal}")
    @Multipart
    suspend fun storePenanganan(
        @Path("id") idLubang: Int,
        @Path("tanggal") tanggal: String,
        @Part("keterangan") keterangan: RequestBody,
        @Part("lat") latitude: RequestBody,
        @Part("long") longitude: RequestBody,
        @Part gambarPenanganan: MultipartBody.Part
    ): Response<ListLubangPenangananResponse>
}