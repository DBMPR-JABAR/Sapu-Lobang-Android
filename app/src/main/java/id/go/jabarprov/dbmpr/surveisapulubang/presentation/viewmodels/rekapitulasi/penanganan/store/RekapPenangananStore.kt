package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetRekapPenanganan
import kotlinx.coroutines.launch
import javax.inject.Inject

class RekapPenangananStore @Inject constructor(private val getRekapPenanganan: GetRekapPenanganan) :
    Store<RekapPenangananAction, RekapPenangananState>(RekapPenangananState()) {
    override fun reduce(action: RekapPenangananAction) {
        coroutineScope.launch {
            when (action) {
                RekapPenangananAction.GetRekapPenanganan -> getRekapPenanganan()
            }
        }
    }

    private suspend fun getRekapPenanganan() {
        state.value = state.value.copy(
            rekapListPenanganan = Resource.Loading()
        )
        val result = getRekapPenanganan.run(None)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    rekapListPenanganan = Resource.Failed(failure.message)
                )
            },
            fnR = { listLubang ->
                state.value = state.value.copy(
                    rekapListPenanganan = Resource.Success(listLubang)
                )
            }
        )
    }
}