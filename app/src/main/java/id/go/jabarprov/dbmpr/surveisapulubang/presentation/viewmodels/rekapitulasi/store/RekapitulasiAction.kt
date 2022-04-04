package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action

sealed class RekapitulasiAction : Action {
    object GetRekapitulasi : RekapitulasiAction()
}