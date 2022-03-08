package id.go.jabarprov.dbmpr.surveisapulubang.data.models.response

import com.google.gson.annotations.SerializedName

data class RuasResponse(
    @SerializedName("id_ruas_jalan") val idRuasJalan: String,
    @SerializedName("nama_ruas_jalan") val namaRuasJalan: String
)
