package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.SurveiLubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.SurveiLubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils

abstract class SurveiLubangDataMapper {
    companion object {
        fun convertSurveiLubangDataResponseToEntity(surveiLubangResponse: SurveiLubangResponse): SurveiLubang {
            return SurveiLubang(
                CalendarUtils.formatStringToCalendar(surveiLubangResponse.tanggal),
                surveiLubangResponse.jumlah
            )
        }
    }
}