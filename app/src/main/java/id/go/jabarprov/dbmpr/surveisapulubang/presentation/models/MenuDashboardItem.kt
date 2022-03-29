package id.go.jabarprov.dbmpr.surveisapulubang.presentation.models

import androidx.annotation.DrawableRes

data class MenuDashboardItem(
    @DrawableRes val image: Int,
    val description: String,
    val buttonText: String,
    val onClickAction: () -> Unit
)
