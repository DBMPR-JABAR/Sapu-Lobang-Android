package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang

data class DetailSurveiState(
    val isFailed: Boolean = false,
    val errorMessage: String = "",
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val listLubang: List<Lubang>? = null,
    val isDelete: Boolean = false
) : State