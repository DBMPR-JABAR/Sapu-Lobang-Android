package id.go.jabarprov.dbmpr.surveisapulubang.data.models.request

import com.google.gson.annotations.SerializedName

data class PenangananRequest(
    val jumlah: Int,
    val tanggal: String,
    @SerializedName("ruas_jalan_id") val idRuasJalan: String
)
