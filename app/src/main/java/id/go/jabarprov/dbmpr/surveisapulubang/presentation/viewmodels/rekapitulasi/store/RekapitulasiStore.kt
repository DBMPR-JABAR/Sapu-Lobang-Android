package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetRekapitulasi
import kotlinx.coroutines.launch
import javax.inject.Inject

class RekapitulasiStore @Inject constructor(private val getRekapitulasi: GetRekapitulasi) :
    Store<RekapitulasiAction, RekapitulasiState>(RekapitulasiState()) {
    override fun reduce(action: RekapitulasiAction) {
        coroutineScope.launch {
            when (action) {
                RekapitulasiAction.GetRekapitulasi -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true
                    )
                    val result = getRekapitulasi.run(None)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false
                            )
                        },
                        fnR = { rekap ->
                            state.value = state.value.copy(
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true,
                                isLoading = false,
                                rekapitulasi = rekap
                            )
                        },
                    )
                }
            }
        }
    }
}