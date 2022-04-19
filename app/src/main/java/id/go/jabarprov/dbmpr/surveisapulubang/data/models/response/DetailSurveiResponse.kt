package id.go.jabarprov.dbmpr.surveisapulubang.data.models.response

import com.google.gson.annotations.SerializedName

data class DetailSurveiResponse(
    @SerializedName("survei_lubang_detail") val listLubang: List<LubangResponse>,
    @SerializedName("survei_potensi_lubang_detail") val listPotensial: List<LubangResponse>
)
