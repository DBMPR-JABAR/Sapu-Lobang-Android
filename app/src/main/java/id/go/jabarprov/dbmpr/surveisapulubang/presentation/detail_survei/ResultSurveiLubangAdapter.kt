package id.go.jabarprov.dbmpr.surveisapulubang.presentation.detail_survei

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutItemLubangResultSurveiBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.KategoriLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl

class ResultSurveiLubangAdapter :
    ListAdapter<Lubang, ResultSurveiLubangAdapter.ResultSurveiItemLubangViewHolder>(
        DIFF_UTIL
    ) {

    private var onDeleteButtonClickListener: ((Lubang) -> Unit)? = null

    private var onDetailButtonClickListener: ((Lubang) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultSurveiItemLubangViewHolder {
        val binding = LayoutItemLubangResultSurveiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResultSurveiItemLubangViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultSurveiItemLubangViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onDetailButtonClickListener, onDeleteButtonClickListener)
        }
    }

    class ResultSurveiItemLubangViewHolder(private val binding: LayoutItemLubangResultSurveiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            lubang: Lubang,
            detailAction: ((Lubang) -> Unit)?,
            deleteAction: ((Lubang) -> Unit)?
        ) {
            binding.apply {

                if (lubang.urlGambar.isNullOrBlank()) {
                    imageView.load(R.drawable.bg_solid_white_container_rounded_corner)
                } else {
                    imageView.load(getSapuLubangImageUrl(lubang.urlGambar)) {
                        transformations(RoundedCornersTransformation(8f))
                        placeholder(R.drawable.bg_solid_white_container_rounded_corner)
                        error(R.drawable.bg_solid_white_container_rounded_corner)
                        build()
                    }
                }

                textViewContentId.text = lubang.id.toString()

                textViewContentMandor.text = "-"

                textViewContentLokasi.text =
                    if (!lubang.kodeLokasi.isNullOrBlank()) {
                        "KM.${lubang.kodeLokasi}. ${lubang.lokasiKm}+${
                            lubang.lokasiM.toString().padStart(3, '0')
                        }"
                    } else {
                        "${lubang.lokasiKm}+${lubang.lokasiM.toString().padStart(3, '0')}"
                    }

                textViewContentKategori.text =
                    if (lubang.kategori == KategoriLubang.SINGLE) "Single" else "Group"

                textViewContentUkuran.text =
                    "${lubang.ukuran?.convertToString()} - ${lubang.kedalaman?.convertToString()}"

                textViewContentPanjangLubang.text = "${lubang.panjang} M"


                buttonDetail.setOnClickListener {
                    detailAction?.invoke(lubang)
                }

                buttonDelete.setOnClickListener {
                    deleteAction?.invoke(lubang)
                }
            }
        }
    }

    fun setOnDeleteButtonClickListener(action: (Lubang) -> Unit): ResultSurveiLubangAdapter {
        onDeleteButtonClickListener = action
        return this
    }

    fun setOnDetailButtonClickListener(action: (Lubang) -> Unit): ResultSurveiLubangAdapter {
        onDetailButtonClickListener = action
        return this
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