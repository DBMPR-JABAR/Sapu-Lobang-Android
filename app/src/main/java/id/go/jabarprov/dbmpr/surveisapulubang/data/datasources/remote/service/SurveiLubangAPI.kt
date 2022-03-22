package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.service

import id.go.jabarprov.dbmpr.surveisapulubang.common.data.remote.models.BaseResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.StartSurveiLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.request.SurveiLubangRequest
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.SurveiLubangResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SurveiLubangAPI {
    @POST("survei-lubang/start")
    suspend fun startSurvei(@Body body: StartSurveiLubangRequest): Response<BaseResponse<SurveiLubangResponse>>

//    @POST("survei-lubang/store/tambah")
//    suspend fun tambahLubang(@Body body: SurveiLubangRequest): Response<BaseResponse<SurveiLubangResponse>>

    @POST("survei-lubang/store/tambah")
    @Multipart
    suspend fun tambahLubang(
        @Part("tanggal") tanggal: RequestBody,
        @Part("ruas_jalan_id") idRuasJalan: RequestBody,
        @Part("jumlah") jumlahLubangPerGroup: RequestBody?,
        @Part("panjang") panjangLubang: RequestBody,
        @Part("lat") latitude: RequestBody,
        @Part("long") longitude: RequestBody,
        @Part("lokasi_kode") kodeLokasi: RequestBody,
        @Part("lokasi_km") lokasiKm: RequestBody,
        @Part("lokasi_m") lokasiM: RequestBody,
        @Part("kategori") kategoriLubang: RequestBody,
        @Part gambarLubang: MultipartBody.Part
    ): Response<BaseResponse<SurveiLubangResponse>>

    @POST("survei-lubang/store/kurang")
    suspend fun kurangLubang(@Body body: SurveiLubangRequest): Response<BaseResponse<SurveiLubangResponse>>
}