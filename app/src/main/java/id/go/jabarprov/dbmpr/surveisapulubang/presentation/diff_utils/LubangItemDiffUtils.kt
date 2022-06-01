package id.go.jabarprov.dbmpr.surveisapulubang.presentation.diff_utils

import androidx.recyclerview.widget.DiffUtil
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang

class LubangItemDiffUtils : DiffUtil.ItemCallback<Lubang>() {
    override fun areItemsTheSame(oldItem: Lubang, newItem: Lubang): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Lubang, newItem: Lubang): Boolean {
        return oldItem == newItem
    }
}