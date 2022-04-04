package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rekapitulasi

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.RekapitulasiResponse

interface RekapitulasiRemoteDataSource {
    suspend fun getRekapitulasi(): RekapitulasiResponse
}