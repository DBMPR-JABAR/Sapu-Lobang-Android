package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import java.util.*

data class RencanaState(
    val idRuasJalan: String = "",
    val ruasJalan: String = "",
    val tanggal: Calendar = Calendar.getInstance(),
    val listLubang: Resource<List<Lubang>> = Resource.Initial(),
    val rejectLubang: Resource<Unit> = Resource.Initial()
) : State