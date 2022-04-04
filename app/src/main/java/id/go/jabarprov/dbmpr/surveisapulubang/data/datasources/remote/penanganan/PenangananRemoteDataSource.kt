package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.penanganan

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import java.io.File
import java.util.*

interface PenangananRemoteDataSource {
    suspend fun storePenanganan(
        idLubang: Int,
        tanggal: Calendar,
        keterangan: String,
        gambarPenanganan: File,
        lat: Double,
        long: Double,
    ): List<LubangResponse>

    suspend fun getListPenangananLubang(
        idRuasJalan: String,
        tanggal: Calendar
    ): List<LubangResponse>
}