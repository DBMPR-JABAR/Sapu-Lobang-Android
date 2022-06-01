package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.kerusakan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang

data class RekapKerusakanState(
    val rekapListLubang: Resource<List<Lubang>> = Resource.Initial(),
    val rekapListPotensi: Resource<List<Lubang>> = Resource.Initial(),
) : State