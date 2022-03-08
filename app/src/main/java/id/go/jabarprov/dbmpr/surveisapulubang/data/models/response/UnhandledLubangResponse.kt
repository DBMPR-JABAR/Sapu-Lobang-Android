package id.go.jabarprov.dbmpr.surveisapulubang.data.models.response

data class UnhandledLubangResponse(
    val created_at: String?,
    val created_by: Int?,
    val id: Int,
    val lat: String,
    val lokasi_km: Int,
    val lokasi_m: Int,
    val long: String,
    val monitoring_lubang_survei_id: Int,
    val ruas_jalan_id: String,
    val status: String?,
    val sup_id: Int,
    val tanggal: String,
    val updated_at: String?,
    val updated_by: Int?,
    val uptd_id: Int
)