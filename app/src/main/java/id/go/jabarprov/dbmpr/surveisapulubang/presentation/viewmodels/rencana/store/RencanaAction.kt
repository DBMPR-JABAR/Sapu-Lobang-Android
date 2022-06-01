package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action

sealed class RencanaAction : Action {
    data class UpdateRuasJalan(val ruasJalan: String) : RencanaAction()
    data class UpdateTanggal(val timeMilles: Long) : RencanaAction()
    object LoadData : RencanaAction()
    data class UploadRencanaLubang(val idLubang: Int, val keterangan: String) : RencanaAction()
    data class RejectLubang(val idLubang: Int) : RencanaAction()
}