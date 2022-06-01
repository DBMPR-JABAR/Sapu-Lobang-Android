package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import kotlinx.coroutines.launch
import javax.inject.Inject

class RekapPenangananStore @Inject constructor() : Store<RekapPenangananAction, RekapPenangananState>(RekapPenangananState()) {
    override fun reduce(action: RekapPenangananAction) {
        coroutineScope.launch {
            when(action) {
                RekapPenangananAction.GetRekapPenanganan -> Unit
            }
        }
    }
}