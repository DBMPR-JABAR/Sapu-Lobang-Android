package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.perencanaan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action

sealed class RekapPerencanaanAction : Action {
    object GetRekapPerencanaan : RekapPerencanaanAction()
}