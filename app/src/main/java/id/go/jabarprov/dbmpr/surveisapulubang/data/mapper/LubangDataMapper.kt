package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils

abstract class LubangDataMapper {
    companion object {
        fun convertLubangDataResponseToEntity(lubang: LubangResponse): Lubang {
            return Lubang(
                id = lubang.id,
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
                status = lubang.status
            )
        }

        fun convertListOfLubangDataResponseToListOfEntity(listLubang: List<LubangResponse>): List<Lubang> {
            return listLubang.map { convertLubangDataResponseToEntity(it) }
        }
    }
}