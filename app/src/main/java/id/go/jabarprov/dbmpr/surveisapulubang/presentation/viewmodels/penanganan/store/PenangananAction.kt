package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action

sealed class PenangananAction : Action {
    data class UpdateRuasJalan(val ruasJalan: String) : PenangananAction()
    data class UpdateTanggal(val timeMilles: Long) : PenangananAction()
    object GetListUnhandledLubang : PenangananAction()
}