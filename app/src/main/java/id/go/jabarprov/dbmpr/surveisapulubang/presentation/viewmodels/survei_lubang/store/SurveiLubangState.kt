package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import java.util.*

data class SurveiLubangState(
    val tanggal: Calendar = Calendar.getInstance(),
    val idRuasJalan: String = "",
    val isFailed: Boolean = false,
    val errorMessage: String = "",
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val jumlahLubang: Int = 0,
    val isStarted: Boolean = false,
    val ruasJalan: String = "",
    val kodeLokasi: String = "",
    val lokasiKm: String = "",
    val lokasiM: String = ""
) : State