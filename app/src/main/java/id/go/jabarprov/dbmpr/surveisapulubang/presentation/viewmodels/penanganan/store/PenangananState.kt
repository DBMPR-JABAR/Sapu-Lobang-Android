package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.UnhandledLubang
import java.util.*

data class PenangananState(
    val tanggal: Calendar = Calendar.getInstance(),
    val idRuasJalan: String = "",
    val isFailed: Boolean = false,
    val errorMessage: String = "",
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val ruasJalan: String = "",
    val listUnhandledLubang: List<UnhandledLubang> = listOf()
) : State