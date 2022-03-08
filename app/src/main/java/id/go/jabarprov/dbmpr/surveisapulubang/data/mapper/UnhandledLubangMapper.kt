package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.UnhandledLubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.UnhandledLubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils

abstract class UnhandledLubangMapper {
    companion object {
        fun convertUnhandledLubangResponseToEntity(unhandledLubangResponse: UnhandledLubangResponse): UnhandledLubang {
            return UnhandledLubang(
                id = unhandledLubangResponse.id,
                tanggal = CalendarUtils.formatStringToCalendar(unhandledLubangResponse.tanggal),
                latitude = unhandledLubangResponse.lat.toDouble(),
                longitude = unhandledLubangResponse.long.toDouble(),
                idRuasJalan = unhandledLubangResponse.ruas_jalan_id,
                lokasiKm = unhandledLubangResponse.lokasi_km,
                lokasiM = unhandledLubangResponse.lokasi_m,
                idSurvei = unhandledLubangResponse.monitoring_lubang_survei_id
            )
        }

        fun convertListOfUnhandledLubangResponseToListOfEntity(listUnhandledLubangResponse: List<UnhandledLubangResponse>): List<UnhandledLubang> {
            return listUnhandledLubangResponse.map { convertUnhandledLubangResponseToEntity(it) }
        }
    }
}