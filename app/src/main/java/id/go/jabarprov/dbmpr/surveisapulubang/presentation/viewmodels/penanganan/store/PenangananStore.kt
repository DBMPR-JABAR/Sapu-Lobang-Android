package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store

import android.util.Log
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetListUnhandledLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.ResolveUnhandledLubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PenangananStore"

class PenangananStore @Inject constructor(
    private val getListUnhandledLubang: GetListUnhandledLubang,
    private val resolveUnhandledLubang: ResolveUnhandledLubang
) :
    Store<PenangananAction, PenangananState>(PenangananState()) {
    override fun reduce(action: PenangananAction) {
        coroutineScope.launch {
            when (action) {
                is PenangananAction.UpdateRuasJalan -> {
                    state.value = state.value.copy(
                        ruasJalan = action.ruasJalan,
                        idRuasJalan = getIdRuasJalan(action.ruasJalan)
                    )
                }
                is PenangananAction.UpdateTanggal -> {
                    state.value = state.value.copy(
                        tanggal = CalendarUtils.formatTimeInMilliesToCalendar(action.timeMilles)
                    )
                }
                is PenangananAction.GetListUnhandledLubang -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true
                    )
                    val param =
                        GetListUnhandledLubang.Params(state.value.tanggal, state.value.idRuasJalan)
                    val result = getListUnhandledLubang.run(param)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false
                            )
                        },
                        fnR = { listUnhandledLubang ->
                            Log.d(TAG, "reduce: $listUnhandledLubang")
                            state.value = state.value.copy(
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true,
                                isLoading = false,
                                listUnhandledLubang = listUnhandledLubang
                            )
                        },
                    )
                }
                is PenangananAction.ResolveUnhandledLubang -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true
                    )
                    val param =
                        ResolveUnhandledLubang.Params(action.idUnhandledLubang, action.tanggal)
                    val result = resolveUnhandledLubang.run(param)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false
                            )
                        },
                        fnR = { listUnhandledLubang ->
                            Log.d(TAG, "reduce: $listUnhandledLubang")
                            state.value = state.value.copy(
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true,
                                isLoading = false,
                                listUnhandledLubang = listUnhandledLubang
                            )
                        },
                    )
                }
            }
        }
    }

    private fun getIdRuasJalan(ruasJalan: String): String {
        return ruasJalan.split(" - ").last()
    }
}