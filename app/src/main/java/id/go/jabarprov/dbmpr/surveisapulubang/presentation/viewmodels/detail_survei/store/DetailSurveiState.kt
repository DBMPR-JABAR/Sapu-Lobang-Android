package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.ResultSurvei

data class DetailSurveiState(
    val resultSurveiState: Resource<ResultSurvei> = Resource.Initial(),
    val deleteItemState: Resource<None> = Resource.Initial(),
    val isDelete: Boolean = false
) : State