package id.go.jabarprov.dbmpr.surveisapulubang.data.models.response

data class LubangResponse(
    val created_at: String?,
    val created_by: Int?,
    val icon: String,
    val id: Int,
    val image: String,
    val jumlah: Int,
    val kategori: String,
    val lat: String,
    val lokasi_km: Int,
    val lokasi_kode: String,
    val lokasi_m: Int,
    val long: String,
    val monitoring_lubang_survei_id: Int,
    val panjang: String,
    val ruas_jalan_id: String,
    val status: String?,
    val sup: String,
    val sup_id: Int,
    val tanggal: String,
    val tanggal_penanganan: String?,
    val tanggal_rencana_penanganan: String?,
    val updated_at: String?,
    val updated_by: String?,
    val uptd_id: Int?,
    val keterangan: String?
)