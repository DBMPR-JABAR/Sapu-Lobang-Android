package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import java.util.*

data class PenangananState(
    val tanggal: Calendar = Calendar.getInstance(),
    val idRuasJalan: String = "",
    val ruasJalan: String = "",
    val listLubang: Resource<List<Lubang>> = Resource.Initial(),
    val storePenanganan: Resource<List<Lubang>> = Resource.Initial(),
    val location: Resource<Unit> = Resource.Initial()
) : State