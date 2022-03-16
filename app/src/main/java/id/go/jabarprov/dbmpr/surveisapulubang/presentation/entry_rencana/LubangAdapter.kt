package id.go.jabarprov.dbmpr.surveisapulubang.presentation.entry_rencana

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutItemLubangBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang

class LubangAdapter(private val type: TYPE) :
    ListAdapter<Lubang, LubangAdapter.LubangItemViewHolder>(
        DIFF_UTIL
    ) {

    private var onItemClickListener: ((Lubang) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LubangItemViewHolder {
        val binding =
            LayoutItemLubangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                .apply {
                    buttonProses.text = when (type) {
                        TYPE.RENCANA -> "Jadwalkan"
                        TYPE.PENANGANAN -> "Proses"
                    }
                }
        return LubangItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LubangItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onItemClickListener)
        }
    }

    fun setOnItemClickListener(action: (Lubang) -> Unit): LubangAdapter {
        return this.apply {
            onItemClickListener = action
        }
    }

    class LubangItemViewHolder(private val binding: LayoutItemLubangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lubang: Lubang, action: ((Lubang) -> Unit)?) {
            binding.apply {
                textViewContentLokasi.text =
                    if (!lubang.kodeLokasi.isNullOrBlank()) {
                        "KM.${lubang.kodeLokasi}. ${lubang.lokasiKm}+${
                            lubang.lokasiM.toString().padStart(3, '0')
                        }"
                    } else {
                        "${lubang.lokasiKm}+${lubang.lokasiM.toString().padStart(3, '0')}"
                    }
                textViewContentLatitude.text = lubang.latitude.toString()
                textViewContentLongitude.text = lubang.longitude.toString()
                buttonProses.setOnClickListener {
                    action?.invoke(lubang)
                }
            }
        }
    }

    enum class TYPE {
        RENCANA,
        PENANGANAN
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Lubang>() {
            override fun areItemsTheSame(oldItem: Lubang, newItem: Lubang): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Lubang, newItem: Lubang): Boolean {
                return oldItem == newItem
            }

        }
    }

}