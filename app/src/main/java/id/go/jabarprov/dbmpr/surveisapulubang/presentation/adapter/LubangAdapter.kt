package id.go.jabarprov.dbmpr.surveisapulubang.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutItemLubangBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.KategoriLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Lubang
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.diff_utils.LubangItemDiffUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import id.go.jabarprov.dbmpr.surveisapulubang.utils.getSapuLubangImageUrl

class LubangAdapter(private val type: TYPE) :
    ListAdapter<Lubang, LubangAdapter.LubangItemViewHolder>(
        LubangItemDiffUtils()
    ) {

    private var onDetailClickListener: ((Lubang) -> Unit)? = null
    private var onDeleteClickListener: ((Lubang) -> Unit)? = null
    private var onProsesClickListener: ((Lubang) -> Unit)? = null
    private var onCheckOnMapClickListener: ((Lubang) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LubangItemViewHolder {
        val binding =
            LayoutItemLubangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                .apply {

                }
        return LubangItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LubangItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(
                it,
                type,
                prosesAction = onProsesClickListener,
                deleteAction = onDeleteClickListener,
                detailAction = onDetailClickListener,
                onCheckOnMapAction = onCheckOnMapClickListener
            )
        }
    }

    fun setOnDeleteClickListener(action: (Lubang) -> Unit): LubangAdapter {
        return this.apply {
            onDeleteClickListener = action
        }
    }

    fun setOnProsesClickListener(action: (Lubang) -> Unit): LubangAdapter {
        return this.apply {
            onProsesClickListener = action
        }
    }

    fun setOnDetailClickListener(action: (Lubang) -> Unit): LubangAdapter {
        return this.apply {
            onDetailClickListener = action
        }
    }

    fun setOnCheckOnMapClickListener(action: (Lubang) -> Unit): LubangAdapter {
        return this.apply {
            onCheckOnMapClickListener = action
        }
    }

    class LubangItemViewHolder(private val binding: LayoutItemLubangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            lubang: Lubang,
            type: TYPE,
            detailAction: ((Lubang) -> Unit)?,
            deleteAction: ((Lubang) -> Unit)?,
            prosesAction: ((Lubang) -> Unit)?,
            onCheckOnMapAction: ((Lubang) -> Unit)?,
        ) {
            binding.apply {

                if (lubang.kategori == KategoriLubang.GROUP) {
                    textViewLabelJumlah.isVisible = true
                    textViewContentJumlah.apply {
                        isVisible = true
                        text = lubang.jumlah.toString()
                    }
                } else {
                    textViewLabelJumlah.isVisible = false
                    textViewContentJumlah.isVisible = false
                }

                if (lubang.urlGambar.isNullOrBlank()) {
                    imageView.load(R.drawable.bg_solid_white_container_rounded_corner)
                } else {
                    val imageUrl =
                        if (type == TYPE.PENANGANAN && lubang.urlGambarPenanganan != null) {
                            lubang.urlGambarPenanganan
                        } else {
                            lubang.urlGambar
                        }
                    imageView.load(getSapuLubangImageUrl(imageUrl)) {
                        transformations(RoundedCornersTransformation(8f))
                        placeholder(R.drawable.bg_solid_white_container_rounded_corner)
                        error(R.drawable.bg_solid_white_container_rounded_corner)
                        build()
                    }
                }

                textViewContentRuas.text = lubang.ruasJalan
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
                textViewContentPanjangLubang.text = "${lubang.panjang} M"
                textViewContentUkuran.text =
                    "${lubang.ukuran?.convertToString()} - ${lubang.kedalaman?.convertToString()}"

                buttonDetail.setOnClickListener {
                    detailAction?.invoke(lubang)
                }

                buttonDelete.setOnClickListener {
                    deleteAction?.invoke(lubang)
                }

                buttonProses.setOnClickListener {
                    prosesAction?.invoke(lubang)
                }

                buttonCekLokasi.setOnClickListener {
                    onCheckOnMapAction?.invoke(lubang)
                }

                when (type) {
                    TYPE.DEFAULT -> {
                        buttonDetail.isVisible = true
                        buttonProses.isVisible = false
                        buttonDelete.isVisible = false
                        buttonCekLokasi.isVisible = false

                        textViewContentTanggal.text =
                            CalendarUtils.formatCalendarToString(lubang.tanggalSurvei)
                        textViewContentMandor.text = lubang.namaMandor
                    }
                    TYPE.SURVEI -> {
                        buttonDetail.isVisible = true
                        buttonProses.isVisible = false
                        buttonDelete.isVisible = true
                        buttonCekLokasi.isVisible = false

                        textViewContentTanggal.text =
                            CalendarUtils.formatCalendarToString(lubang.tanggalSurvei)
                        textViewLabelMandor.isVisible = false
                        textViewContentMandor.isVisible = false
                    }
                    TYPE.RENCANA -> {
                        buttonDetail.isVisible = true
                        buttonProses.isVisible = true
                        buttonCekLokasi.isVisible = true

                        textViewContentMandor.text = lubang.namaMandor

                        if (lubang.status == null) {
                            textViewContentTanggal.text =
                                CalendarUtils.formatCalendarToString(lubang.tanggalSurvei)
                            buttonDelete.isVisible = true
                            buttonDelete.text = "Tolak"

                            buttonProses.text = "Jadwalkan"
                            buttonProses.isEnabled = true
                        } else {
                            textViewContentTanggal.text = lubang.tanggalPerencanaan?.let {
                                CalendarUtils.formatCalendarToString(
                                    it
                                )
                            }
                            buttonDelete.isVisible = false
                            buttonProses.text = "Sudah Dijadwalkan"
                            buttonProses.isEnabled = false
                        }
                    }
                    TYPE.PENANGANAN -> {
                        buttonDetail.isVisible = true
                        buttonProses.isVisible = true
                        buttonDelete.isVisible = false
                        buttonCekLokasi.isVisible = true

                        textViewContentMandor.text = lubang.namaMandor

                        if (lubang.status == "Perencanaan") {
                            textViewContentTanggal.text = lubang.tanggalPerencanaan?.let {
                                CalendarUtils.formatCalendarToString(
                                    it
                                )
                            }
                            buttonProses.text = "Tangani"
                            buttonProses.isEnabled = true
                        } else {
                            textViewContentTanggal.text = lubang.tanggalPenanganan?.let {
                                CalendarUtils.formatCalendarToString(
                                    it
                                )
                            }
                            buttonProses.text = "Sudah Ditangani"
                            buttonProses.isEnabled = false
                        }
                    }
                }
            }
        }
    }

    enum class TYPE {
        DEFAULT,
        SURVEI,
        RENCANA,
        PENANGANAN
    }

}