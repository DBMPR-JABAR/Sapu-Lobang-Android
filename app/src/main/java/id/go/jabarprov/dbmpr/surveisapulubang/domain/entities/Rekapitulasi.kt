package id.go.jabarprov.dbmpr.surveisapulubang.domain.entities

data class Rekapitulasi(
    val jumlah: Jumlah,
    val panjang: Panjang
) {
    data class Jumlah(
        val sisa: Int,
        val perencanaan: Int,
        val penanganan: Int,
        val potensi: Int
    )

    data class Panjang(
        val sisa: Double,
        val perencanaan: Double,
        val penanganan: Double,
        val potensi: Double
    )
}