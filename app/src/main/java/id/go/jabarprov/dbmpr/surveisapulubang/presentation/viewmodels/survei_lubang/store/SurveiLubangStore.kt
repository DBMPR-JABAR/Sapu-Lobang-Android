package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.KategoriLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.KurangLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.StartSurvei
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.TambahLubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class SurveiLubangStore @Inject constructor(
    private val startSurvei: StartSurvei,
    private val tambahLubang: TambahLubang,
    private val kurangLubang: KurangLubang
) : Store<SurveiLubangAction, SurveiLubangState>(SurveiLubangState()) {
    override fun reduce(action: SurveiLubangAction) {
        coroutineScope.launch {
            when (action) {
                is SurveiLubangAction.StartSurveiAction -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true
                    )
                    val param = StartSurvei.Params(action.tanggal, action.idRuasJalan)
                    val result = startSurvei.run(param)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false
                            )
                        },
                        fnR = { surveiLubang ->
                            state.value = state.value.copy(
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true,
                                isLoading = false,
                                jumlahLubangTotal = surveiLubang.jumlahTotal,
                                panjangLubangTotal = surveiLubang.panjangTotal,
                                isStarted = true
                            )
                        }
                    )
                }

                is SurveiLubangAction.TambahLubangAction -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true
                    )
                    val param = TambahLubang.Params(
                        tanggal = state.value.tanggal,
                        idRuasJalan = state.value.idRuasJalan,
                        kodeLokasi = state.value.kodeLokasi,
                        lokasiKm = state.value.lokasiKm,
                        lokasiM = state.value.lokasiM,
                        lat = action.lat,
                        long = action.long,
                        panjangLubang = state.value.panjangLubang,
                        jumlahLubangPerGroup = state.value.jumlahLubangPerGroup,
                        kategoriLubang = if (state.value.kategoriLubang == KategoriLubang.SINGLE) "Single" else "Group",
                        gambarLubang = state.value.gambarLubangFile!!,
                        keterangan = state.value.keteranganLubang
                    )
                    val result = tambahLubang.run(param)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false
                            )
                        },
                        fnR = { surveiLubang ->
                            state.value = state.value.copy(
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true,
                                isLoading = false,
                                jumlahLubangTotal = surveiLubang.jumlahTotal,
                                panjangLubangTotal = surveiLubang.panjangTotal,
                                gambarLubangFile = null,
                                gambarLubangUri = null,
                                keteranganLubang = null,
                                panjangLubang = 0.0,
                                jumlahLubangPerGroup = 0,
                                isResetting = true
                            )
                        }
                    )
                }

                is SurveiLubangAction.KurangLubangAction -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true
                    )
                    val param = KurangLubang.Params(
                        state.value.tanggal,
                        state.value.idRuasJalan,
                        state.value.kodeLokasi,
                        state.value.lokasiKm,
                        state.value.lokasiM,
                        action.lat,
                        action.long
                    )
                    val result = kurangLubang.run(param)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false,
                            )
                        },
                        fnR = { surveiLubang ->
                            state.value = state.value.copy(
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true,
                                isLoading = false,
                                jumlahLubangTotal = surveiLubang.jumlahTotal,
                                panjangLubangTotal = surveiLubang.panjangTotal
                            )
                        }
                    )
                }

                is SurveiLubangAction.UpdateRuasJalan -> {
                    state.value = state.value.copy(
                        idRuasJalan = getIdRuasJalan(action.ruasJalan),
                        ruasJalan = action.ruasJalan
                    )
                }

                is SurveiLubangAction.UpdateTanggal -> {
                    state.value = state.value.copy(
                        tanggal = CalendarUtils.formatTimeInMilliesToCalendar(action.timeInMillis)
                    )
                }

                is SurveiLubangAction.UpdateKodeLokasi -> {
                    state.value = state.value.copy(
                        kodeLokasi = action.kodeLokasi
                    )
                }

                is SurveiLubangAction.UpdateLokasiKm -> {
                    state.value = state.value.copy(
                        lokasiKm = action.lokasiKm
                    )
                }

                is SurveiLubangAction.UpdateLokasiM -> {
                    state.value = state.value.copy(
                        lokasiM = action.lokasiM
                    )
                }

                is SurveiLubangAction.UpdateFotoLubang -> {
                    state.value = state.value.copy(
                        gambarLubangUri = action.gambarLubang,
                        gambarLubangFile = action.gambarLubangFile
                    )
                }

                is SurveiLubangAction.UpdateKategoriLubang -> {
                    state.value = state.value.copy(
                        kategoriLubang = action.kategoriLubang
                    )
                }

                is SurveiLubangAction.UpdatePanjangLubang -> {
                    state.value = state.value.copy(
                        panjangLubang = action.panjangLubang
                    )
                }
                is SurveiLubangAction.UpdateJumlahLubangPerGroup -> {
                    state.value = state.value.copy(
                        jumlahLubangPerGroup = action.jumlahLubangPerGroup
                    )
                }
                is SurveiLubangAction.UpdateKeterangan -> {
                    state.value = state.value.copy(
                        keteranganLubang = action.keterangan
                    )
                }
                SurveiLubangAction.InputLubangResetted -> {
                    state.value = state.value.copy(
                        isResetting = false
                    )
                }
            }
        }
    }

    private fun getIdRuasJalan(ruasJalan: String): String {
        return ruasJalan.split(" - ").last()
    }
}