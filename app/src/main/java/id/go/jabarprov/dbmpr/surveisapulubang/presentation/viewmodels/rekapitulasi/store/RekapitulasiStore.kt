package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetRekapitulasi
import kotlinx.coroutines.launch
import javax.inject.Inject

class RekapitulasiStore @Inject constructor(private val getRekapitulasi: GetRekapitulasi) :
    Store<RekapitulasiAction, RekapitulasiState>(RekapitulasiState()) {
    override fun reduce(action: RekapitulasiAction) {
        coroutineScope.launch {
            when (action) {
                RekapitulasiAction.GetRekapitulasi -> getRekapitulasi()
            }
        }
    }

    private suspend fun getRekapitulasi() {
        state.value = state.value.copy(
            rekapState = Resource.Loading()
        )
        val result = getRekapitulasi.run(None)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    rekapState = Resource.Failed(failure.message)
                )
            },
            fnR = { rekap ->
                state.value = state.value.copy(
                    rekapState = Resource.Success(rekap)
                )
            },
        )
    }
}