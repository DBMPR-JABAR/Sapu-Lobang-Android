package id.go.jabarprov.dbmpr.surveisapulubang.presentation.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.LayoutItemMenuDashboardBinding
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.models.MenuDashboardItem

class MenuDashboardAdapter(private val listMenuDashboardItem: List<MenuDashboardItem>) :
    RecyclerView.Adapter<MenuDashboardAdapter.MenuDashboardItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuDashboardItemViewHolder {
        val binding = LayoutItemMenuDashboardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuDashboardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuDashboardItemViewHolder, position: Int) {
        holder.bind(listMenuDashboardItem[position])
    }

    override fun getItemCount(): Int = listMenuDashboardItem.size

    class MenuDashboardItemViewHolder(private val binding: LayoutItemMenuDashboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menuDashboardItem: MenuDashboardItem) {
            binding.apply {
                imageViewLogo.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        menuDashboardItem.image
                    )
                )
                textViewLabel.text = menuDashboardItem.description
                buttonMenu.text = menuDashboardItem.buttonText
                buttonMenu.setOnClickListener {
                    menuDashboardItem.onClickAction()
                }
            }
        }
    }

}