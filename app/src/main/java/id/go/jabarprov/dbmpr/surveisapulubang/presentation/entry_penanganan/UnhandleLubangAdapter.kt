package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_penanganan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutItemLubangBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.UnhandledLubang

class UnhandleLubangAdapter :
    ListAdapter<UnhandledLubang, UnhandleLubangAdapter.UnhandleLubangItemViewHolder>(
        DIFF_UTIL
    ) {

    private var onClickListener: ((UnhandledLubang) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UnhandleLubangItemViewHolder {
        val binding =
            LayoutItemLubangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnhandleLubangItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UnhandleLubangItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClickListener)
        }
    }

    fun setOnClickListener(action: (UnhandledLubang) -> Unit) {
        onClickListener = action
    }

    class UnhandleLubangItemViewHolder(private val binding: LayoutItemLubangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(unhandledLubang: UnhandledLubang, action: ((UnhandledLubang) -> Unit)?) {
            binding.apply {
                textViewContentLokasi.text =
                    "${unhandledLubang.lokasiKm}+${unhandledLubang.lokasiM}"
                textViewContentLatitude.text = unhandledLubang.latitude.toString()
                textViewContentLongitude.text = unhandledLubang.longitude.toString()
                buttonSelesai.setOnClickListener {
                    action?.invoke(unhandledLubang)
                }
            }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<UnhandledLubang>() {
            override fun areItemsTheSame(
                oldItem: UnhandledLubang,
                newItem: UnhandledLubang
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UnhandledLubang,
                newItem: UnhandledLubang
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}