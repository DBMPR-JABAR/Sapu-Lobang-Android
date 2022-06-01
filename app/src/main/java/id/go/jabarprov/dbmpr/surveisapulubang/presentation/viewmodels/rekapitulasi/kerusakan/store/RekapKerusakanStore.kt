package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.kerusakan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetRekapLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetRekapPotensi
import kotlinx.coroutines.launch
import javax.inject.Inject

class RekapKerusakanStore @Inject constructor(
    private val getRekapLubang: GetRekapLubang,
    private val getRekapPotensi: GetRekapPotensi
) :
    Store<RekapKerusakanAction, RekapKerusakanState>(RekapKerusakanState()) {

    override fun reduce(action: RekapKerusakanAction) {
        coroutineScope.launch {
            when (action) {
                RekapKerusakanAction.GetRekapLubang -> getRekapLubang()
                RekapKerusakanAction.GetRekapPotensi -> getRekapPotensi()
            }
        }
    }

    private suspend fun getRekapLubang() {
        state.value = state.value.copy(
            rekapListLubang = Resource.Loading()
        )
        val result = getRekapLubang.run(None)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    rekapListLubang = Resource.Failed(failure.message)
                )
            },
            fnR = { listLubang ->
                state.value = state.value.copy(
                    rekapListLubang = Resource.Success(listLubang)
                )
            }
        )
    }

    private suspend fun getRekapPotensi() {
        state.value = state.value.copy(
            rekapListPotensi = Resource.Loading()
        )
        val result = getRekapPotensi.run(None)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    rekapListPotensi = Resource.Failed(failure.message)
                )
            },
            fnR = { listLubang ->
                state.value = state.value.copy(
                    rekapListPotensi = Resource.Success(listLubang)
                )
            }
        )
    }
}