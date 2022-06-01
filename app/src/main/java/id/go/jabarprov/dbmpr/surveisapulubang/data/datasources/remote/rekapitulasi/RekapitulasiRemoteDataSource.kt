package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rekapitulasi

import id.go.jabarprov.dbmpr.surveisapulubang.common.data.remote.models.BaseResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.RekapitulasiResponse
import retrofit2.Response
import retrofit2.http.GET

interface RekapitulasiRemoteDataSource {
    suspend fun getRekapitulasi(): RekapitulasiResponse
    suspend fun getRekapitulasiLubang(): List<LubangResponse>
    suspend fun getRekapitulasiPotensi(): List<LubangResponse>
    suspend fun getRekapitulasiPerencanaan(): List<LubangResponse>
    suspend fun getRekapitulasiPenanganan(): List<LubangResponse>
}