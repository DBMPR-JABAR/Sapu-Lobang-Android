package id.go.jabarprov.dbmpr.surveisapulubang.data.models.request

import com.google.gson.annotations.SerializedName

data class SurveiLubangRequest(
    val tanggal: String,
    @SerializedName("ruas_jalan_id") val idRuasJalan: String,
    @SerializedName("lokasi_kode") val kodeLokasi: String,
    @SerializedName("lokasi_km") val lokasiKm: String,
    @SerializedName("lokasi_m") val lokasiM: String,
    val lat: Double? = null,
    val long: Double? = null
)
