package com.naufal.capstonewasteclassification.ui.chart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.naufal.capstonewasteclassification.R
import com.naufal.capstonewasteclassification.databinding.FragmentChartBinding
import com.naufal.capstonewasteclassification.ui.checkout.CheckoutActivity
import java.text.NumberFormat
import java.util.Locale

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    private lateinit var chartViewModel: ChartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerViewShoppingItems
        recyclerView.layoutManager = LinearLayoutManager(context)
        val wasteListAdapter = WasteListAdapter(createWasteItemList()) { wasteItem, count ->
            updateTotalPriceOnCheckoutButton()
        }
        recyclerView.adapter = wasteListAdapter

        val textView: TextView = binding.textProfile
        chartViewModel = ViewModelProvider(this).get(ChartViewModel::class.java)

        chartViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val checkoutButton: Button = binding.checkoutButton
        checkoutButton.setOnClickListener {
            checkout()
        }

        return root
    }

    private fun createWasteItemList(): List<WasteItem> {
        val wasteItemLists = mutableListOf(
            WasteItem("Gelas", 0, R.drawable.glass, 200),
            WasteItem("Plastik", 0, R.drawable.plastic, 100),
            WasteItem("Kaleng", 0, R.drawable.kaleng, 200),
            WasteItem("Kertas", 0, R.drawable.kertas, 50),
            WasteItem("Kayu", 0, R.drawable.kayu, 1000),
            WasteItem("Kardus", 0, R.drawable.kardus, 1000),


            // Add more items with their respective image resources and prices
        )
        return wasteItemLists
    }

    private fun checkout() {
        val selectedItems = (binding.recyclerViewShoppingItems.adapter as WasteListAdapter).getSelectedItems()

        // Calculate total price in Rupiah
        var totalPrice = 0.0
        for ((item, quantity) in selectedItems) {
            totalPrice += item.price * quantity
        }

        // Start CheckoutActivity and pass the total price
        val intent = Intent(requireContext(), CheckoutActivity::class.java)
        intent.putExtra("TOTAL_PRICE", totalPrice)
        startActivity(intent)
    }

    private fun updateTotalPriceOnCheckoutButton() {
        val selectedItems = (binding.recyclerViewShoppingItems.adapter as WasteListAdapter).getSelectedItems()

        // Calculate total price in Rupiah
        var totalPrice = 0.0
        for ((item, quantity) in selectedItems) {
            totalPrice += item.price * quantity
        }

        // Display total price in Rupiah on the button
        binding.checkoutButton.text = "Total Jual: ${formatToRupiah(totalPrice)}"
    }

    private fun formatToRupiah(price: Double): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(price)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}