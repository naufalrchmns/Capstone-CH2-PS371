package com.naufal.capstonewasteclassification.ui.chart

import java.text.NumberFormat
import java.util.Locale

data class WasteItem(val name: String, var count: Int, val imageResId: Int, val price: Int) {
    // Format the price as Indonesian Rupiah
    fun formattedPrice(): String {
        val localeID = Locale("id", "ID")
        val currencyFormat = NumberFormat.getCurrencyInstance(localeID)
        return currencyFormat.format(price)
    }
}

