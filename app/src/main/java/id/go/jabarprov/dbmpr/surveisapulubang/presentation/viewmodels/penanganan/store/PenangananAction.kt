package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action
import java.util.*

sealed class PenangananAction : Action {
    data class UpdateRuasJalan(val ruasJalan: String) : PenangananAction()
    data class UpdateTanggal(val timeMilles: Long) : PenangananAction()
    object GetListLubang : PenangananAction()
    data class StorePenangananLubang(
        val idLubang: Int,
        val keterangan: String
    ) : PenangananAction()
}