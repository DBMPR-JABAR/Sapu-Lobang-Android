package id.go.jabarprov.dbmpr.surveisapulubang.data.models.response

import com.google.gson.annotations.SerializedName

data class ListLubangPenangananResponse(
    @SerializedName("success") val isSuccess: Boolean,
    val message: String,
    @SerializedName("data") val listLubang: List<LubangResponse>,
    @SerializedName("data_selesai") val listFinishedLubang: List<LubangResponse>
)
