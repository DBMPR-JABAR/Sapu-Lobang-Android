package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.*
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils

abstract class LubangDataMapper {
    companion object {
        fun convertLubangDataResponseToEntity(lubang: LubangResponse): Lubang {
            return Lubang(
                id = lubang.id,
                namaMandor = lubang.user_create?.name ?: "-",
                tanggalSurvei = CalendarUtils.formatStringToCalendar(lubang.tanggal),
                tanggalPerencanaan = if (!lubang.tanggal_rencana_penanganan.isNullOrEmpty()) CalendarUtils.formatStringToCalendar(
                    lubang.tanggal_rencana_penanganan
                ) else null,
                tanggalPenanganan = if (!lubang.tanggal_penanganan.isNullOrEmpty()) CalendarUtils.formatStringToCalendar(
                    lubang.tanggal_penanganan
                ) else null,
                latitude = lubang.lat.toDouble(),
                longitude = lubang.long.toDouble(),
                kodeLokasi = lubang.lokasi_kode,
                lokasiKm = lubang.lokasi_km,
                lokasiM = lubang.lokasi_m,
                idSurvei = lubang.monitoring_lubang_survei_id,
                idRuasJalan = lubang.ruas_jalan_id,
                status = lubang.status,
                panjang = lubang.panjang.toDouble(),
                jumlah = lubang.jumlah,
                kategori = if (lubang.kategori == "Group") KategoriLubang.GROUP else KategoriLubang.SINGLE,
                urlGambar = lubang.image,
                keterangan = lubang.keterangan,
                urlGambarPenanganan = lubang.image_penanganan,
                lajur = if (!lubang.lajur.isNullOrBlank()) Lajur.convertStringToLajur(lubang.lajur) else null,
                ukuran = if (!lubang.kategori_kedalaman.isNullOrBlank()) Ukuran.convertStringToUkuran(
                    lubang.kategori_kedalaman.split(" - ")[0]
                ) else null,
                kedalaman = if (!lubang.kategori_kedalaman.isNullOrBlank()) Kedalaman.convertStringToUkuran(
                    lubang.kategori_kedalaman.split(" - ")[1]
                ) else null,
                potensi = (lubang.potensi_lubang == "true") || (lubang.potensi_lubang == "1"),
                ruasJalan = lubang.ruas?.namaRuasJalan ?: "-"
            )
        }

        fun convertListOfLubangDataResponseToListOfEntity(listLubang: List<LubangResponse>): List<Lubang> {
            return listLubang.map { convertLubangDataResponseToEntity(it) }
        }
    }
}