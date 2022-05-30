package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.ResultSurveiResponse
import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.SurveiLubangResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.ResultSurvei
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.SurveiLubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils

abstract class SurveiLubangDataMapper {
    companion object {
        fun convertSurveiLubangDataResponseToEntity(surveiLubangResponse: SurveiLubangResponse): SurveiLubang {
            return SurveiLubang(
                CalendarUtils.formatStringToCalendar(surveiLubangResponse.tanggal),
                surveiLubangResponse.jumlah,
                surveiLubangResponse.panjang
            )
        }

        fun convertResultSurveiResponseToEntity(resultSurveiResponse: ResultSurveiResponse): ResultSurvei {
            return ResultSurvei(
                ruas = RuasDataMapper.convertRuasDataResponseToEntity(resultSurveiResponse.ruas),
                listLubang = LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(
                    resultSurveiResponse.listLubang
                ),
                listPotensiLubang = LubangDataMapper.convertListOfLubangDataResponseToListOfEntity(
                    resultSurveiResponse.listPotensial
                )
            )
        }
    }
}