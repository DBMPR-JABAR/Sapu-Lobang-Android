package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi

data class RekapitulasiState(
    val isFailed: Boolean = false,
    val errorMessage: String = "",
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val rekapitulasi: Rekapitulasi? = null
) : State