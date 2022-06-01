package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi

data class RekapitulasiState(
    val rekapState: Resource<Rekapitulasi> = Resource.Initial(),


) : State