package id.go.jabarprov.dbmpr.surveisapulubang.domain.entities

import java.util.*

data class Lubang(
    val id: Int,
    val tanggalSurvei: Calendar,
    val tanggalPerencanaan: Calendar?,
    val tanggalPenanganan: Calendar?,
    val latitude: Double,
    val longitude: Double,
    val kodeLokasi: String?,
    val lokasiKm: Int,
    val lokasiM: Int,
    val idSurvei: Int,
    val idRuasJalan: String,
    val status: String?
)
