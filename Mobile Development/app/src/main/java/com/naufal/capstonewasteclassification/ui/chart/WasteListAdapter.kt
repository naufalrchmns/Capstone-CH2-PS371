package com.naufal.capstonewasteclassification.ui.chart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.naufal.capstonewasteclassification.R
import java.text.NumberFormat
import java.util.Locale

class WasteListAdapter(
    private val wasteItemList: List<WasteItem>,
    private val quantityChangeListener: (WasteItem, Int) -> Unit
) : RecyclerView.Adapter<WasteListAdapter.ViewHolder>() {

    // Keep track of selected items and their quantities
    private val selectedItems = mutableMapOf<WasteItem, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_waste_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wasteItem = wasteItemList[position]

        holder.itemImage.setImageResource(wasteItem.imageResId)
        holder.textWasteItem.text = wasteItem.name
        holder.textItemPrice.text = formatToRupiah(wasteItem.price) // Display the price
        holder.textItemCount.text = wasteItem.count.toString()

        holder.btnIncrease.setOnClickListener {
            wasteItem.count++
            updateSelectedItems(wasteItem, wasteItem.count)
            notifyDataSetChanged()
        }

        holder.btnDecrease.setOnClickListener {
            if (wasteItem.count > 0) {
                wasteItem.count--
                updateSelectedItems(wasteItem, wasteItem.count)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = wasteItemList.size

    fun getSelectedItems(): Map<WasteItem, Int> {
        return selectedItems.toMap()
    }

    private fun updateSelectedItems(wasteItem: WasteItem, count: Int) {
        if (count > 0) {
            selectedItems[wasteItem] = count
        } else {
            selectedItems.remove(wasteItem)
        }

        // Notify the quantity change to the listener
        quantityChangeListener.invoke(wasteItem, count)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val textWasteItem: TextView = itemView.findViewById(R.id.textWasteItem)
        val textItemPrice: TextView = itemView.findViewById(R.id.textItemPrice)
        val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        val textItemCount: TextView = itemView.findViewById(R.id.textItemCount)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
    }

    private fun formatToRupiah(price: Int): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(price)
    }
}