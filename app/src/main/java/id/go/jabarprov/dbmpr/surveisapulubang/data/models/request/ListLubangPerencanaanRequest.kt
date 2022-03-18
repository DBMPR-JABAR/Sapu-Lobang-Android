package id.go.jabarprov.dbmpr.surveisapulubang.data.models.request

import com.google.gson.annotations.SerializedName

data class ListLubangPerencanaanRequest(
    @SerializedName("ruas_jalan_id") val idRuasJalan: String,
    @SerializedName("tanggal") val tanggal: String
)
