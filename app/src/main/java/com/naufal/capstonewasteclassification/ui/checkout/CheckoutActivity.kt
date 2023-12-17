package com.naufal.capstonewasteclassification.ui.checkout

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.naufal.capstonewasteclassification.R
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var textCheckoutTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Initialize textCheckoutTotal
        textCheckoutTotal = findViewById(R.id.textCheckoutTotal)

        // Retrieve total price from intent
        val totalPrice = intent.getDoubleExtra("TOTAL_PRICE", 0.0)

        // Display total price in Rupiah on checkout text view
        textCheckoutTotal.text = "Total: ${formatToRupiah(totalPrice)}"

        // Enable the back button in the ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle the back button click
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun formatToRupiah(price: Double): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(price)
    }
}