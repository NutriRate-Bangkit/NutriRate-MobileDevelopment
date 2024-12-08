package com.example.nutrirateapp.view.main.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrirateapp.R

class HistoryAdapter(private val data: List<HistoryItem>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        val textViewProductName: TextView = itemView.findViewById(R.id.textViewProductName)
        val textViewProductGrade: TextView = itemView.findViewById(R.id.textViewProductGrade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textViewProductName.text = item.productName
        holder.textViewProductGrade.text = item.productGrade
        holder.imageViewProduct.setImageResource(item.productImage)
    }

    override fun getItemCount(): Int = data.size
}

data class HistoryItem(
    val productImage: Int,
    val productName: String,
    val productGrade: String
)
