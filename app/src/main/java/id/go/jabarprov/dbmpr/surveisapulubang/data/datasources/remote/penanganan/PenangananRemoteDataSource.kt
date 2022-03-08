package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.penanganan

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.UnhandledLubangResponse
import java.util.*

interface PenangananRemoteDataSource {
    suspend fun storePenanganan(tanggal: Calendar, idRuasJalan: String, jumlah: Int)
    suspend fun getListUnhandledLubang(
        idRuasJalan: String,
        tanggal: Calendar
    ): List<UnhandledLubangResponse>

    suspend fun resolveUnhandledLubang(
        idUnhandledLubang: Int,
        tanggal: Calendar,
        keterangan: String,
    ): List<UnhandledLubangResponse>
}