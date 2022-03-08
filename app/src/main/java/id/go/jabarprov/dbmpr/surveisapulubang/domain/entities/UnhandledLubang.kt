package id.go.jabarprov.dbmpr.surveisapulubang.domain.entities

import java.util.*

data class UnhandledLubang(
    val id: Int,
    val tanggal: Calendar,
    val latitude: Double,
    val longitude: Double,
    val idRuasJalan: String,
    val lokasiKm: Int,
    val lokasiM: Int,
    val idSurvei: Int
)
