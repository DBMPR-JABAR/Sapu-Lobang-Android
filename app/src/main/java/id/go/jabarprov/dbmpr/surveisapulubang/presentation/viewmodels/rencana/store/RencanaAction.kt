package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action

sealed class RencanaAction : Action {
    data class UpdateRuasJalan(val ruasJalan: String) : RencanaAction()
    data class UpdateTanggal(val timeMilles: Long) : RencanaAction()
    data class UpdateJumlahRencanaPenanganan(val jumlah: Int) : RencanaAction()
    object UploadRencanaPenanganan : RencanaAction()
}