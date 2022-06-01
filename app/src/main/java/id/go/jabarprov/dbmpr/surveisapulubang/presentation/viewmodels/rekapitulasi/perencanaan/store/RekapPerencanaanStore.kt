package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.perencanaan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetRekapPerencanaan
import kotlinx.coroutines.launch
import javax.inject.Inject

class RekapPerencanaanStore @Inject constructor(private val getRekapPerencanaan: GetRekapPerencanaan) :
    Store<RekapPerencanaanAction, RekapPerencanaanState>(
        RekapPerencanaanState()
    ) {
    override fun reduce(action: RekapPerencanaanAction) {
        coroutineScope.launch {
            when (action) {
                RekapPerencanaanAction.GetRekapPerencanaan -> getRekapPerencanaan()
            }
        }
    }

    private suspend fun getRekapPerencanaan() {
        state.value = state.value.copy(
            rekapListPerencanaan = Resource.Loading()
        )
        val result = getRekapPerencanaan.run(None)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    rekapListPerencanaan = Resource.Failed(failure.message)
                )
            },
            fnR = { listLubang ->
                state.value = state.value.copy(
                    rekapListPerencanaan = Resource.Success(listLubang)
                )
            }
        )
    }
}