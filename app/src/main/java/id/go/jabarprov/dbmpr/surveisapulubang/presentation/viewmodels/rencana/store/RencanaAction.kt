package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action
import java.util.*

sealed class RencanaAction : Action {
    data class UpdateRuasJalan(val ruasJalan: String) : RencanaAction()
    data class UpdateTanggal(val timeMilles: Long) : RencanaAction()
    object LoadData : RencanaAction()
    data class UploadRencanaLubang(val idLubang: Int, val tanggal: Calendar, val keterangan: String) : RencanaAction()
}