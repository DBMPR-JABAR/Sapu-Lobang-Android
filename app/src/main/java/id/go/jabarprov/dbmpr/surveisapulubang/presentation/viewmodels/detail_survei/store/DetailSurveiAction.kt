package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action
import java.util.*

sealed class DetailSurveiAction : Action {
    data class LoadSurveiData(val idRuasJalan: String, val tanggal: Calendar) : DetailSurveiAction()
    data class DeleteLubang(val idLubang: Int) : DetailSurveiAction()
    data class DeletePotensiLubang(val idLubang: Int) : DetailSurveiAction()
}