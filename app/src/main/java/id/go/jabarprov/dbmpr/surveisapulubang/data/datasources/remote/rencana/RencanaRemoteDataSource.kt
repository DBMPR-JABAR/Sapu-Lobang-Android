package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rencana

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import java.util.*

interface RencanaRemoteDataSource {
    suspend fun getListLubang(tanggal: Calendar, idRuasJalan: String): List<LubangResponse>
    suspend fun storeRencana(idLubang: Int, tanggal: Calendar, keterangan: String): List<LubangResponse>
}