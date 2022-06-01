package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action

sealed class RekapPenangananAction : Action {
    object GetRekapPenanganan : RekapPenangananAction()
}