package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action

sealed class PenangananAction : Action {
    data class UpdateRuasJalan(val ruasJalan: String) : PenangananAction()
    data class UpdateTanggal(val timeMilles: Long) : PenangananAction()
    data class UpdateKodeLokasi(val kodeLokasi: String) : PenangananAction()
    data class UpdateLokasiKm(val lokasiKm: String) : PenangananAction()
    data class UpdateLokasiM(val lokasiM: String) : PenangananAction()
    object TambahLubang : PenangananAction()
    object KurangLubang : PenangananAction()
}