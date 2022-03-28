package id.go.jabarprov.dbmpr.surveisapulubang.domain.entities

import java.util.*

data class Lubang(
    val id: Int,
    val tanggalSurvei: Calendar,
    val tanggalPerencanaan: Calendar?,
    val tanggalPenanganan: Calendar?,
    val latitude: Double,
    val longitude: Double,
    val kodeLokasi: String?,
    val lokasiKm: Int,
    val lokasiM: Int,
    val idSurvei: Int,
    val idRuasJalan: String,
    val status: String?,
    val panjang: Double,
    val jumlah: Int,
    val keterangan: String?,
    val kategori: KategoriLubang,
    val urlGambar: String?,
    val urlGambarPenanganan: String?,
    val lajur: Lajur?,
    val ukuran: Ukuran?,
    val kedalaman: Kedalaman?
)

enum class KategoriLubang {
    SINGLE,
    GROUP
}

enum class Lajur {
    KIRI,
    KANAN;

    fun convertToString(): String {
        return when (this) {
            KIRI -> "Kiri"
            KANAN -> "Kanan"
        }
    }

    companion object {
        fun convertStringToLajur(lajur: String): Lajur {
            return when (lajur) {
                "Kiri" -> KIRI
                else -> KANAN
            }
        }
    }
}

enum class Ukuran {
    KECIL,
    BESAR;

    fun convertToString(): String {
        return when (this) {
            KECIL -> "Kecil"
            BESAR -> "Besar"
        }
    }

    companion object {
        fun convertStringToUkuran(ukuran: String): Ukuran {
            return when (ukuran) {
                "Kecil" -> KECIL
                else -> BESAR
            }
        }
    }
}

enum class Kedalaman {
    DANGKAL,
    DALAM;

    fun convertToString(): String {
        return when (this) {
            DANGKAL -> "Dangkal"
            DALAM -> "Dalam"
        }
    }

    companion object {
        fun convertStringToUkuran(kedalaman: String): Kedalaman {
            return when (kedalaman) {
                "Dangkal" -> DANGKAL
                else -> DALAM
            }
        }
    }
}
