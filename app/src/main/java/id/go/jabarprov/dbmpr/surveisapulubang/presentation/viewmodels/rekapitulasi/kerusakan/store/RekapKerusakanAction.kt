package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.kerusakan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action

sealed class RekapKerusakanAction : Action {
    object GetRekapLubang : RekapKerusakanAction()
    object GetRekapPotensi : RekapKerusakanAction()
}