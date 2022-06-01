package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.perencanaan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import kotlinx.coroutines.launch
import javax.inject.Inject

class RekapPerencanaanStore @Inject constructor() : Store<RekapPerencanaanAction, RekapPerencanaanState>(
    RekapPerencanaanState()
) {
    override fun reduce(action: RekapPerencanaanAction) {
        coroutineScope.launch {
            when (action) {
                RekapPerencanaanAction.GetRekapPerencanaan -> Unit
            }
        }
    }
}