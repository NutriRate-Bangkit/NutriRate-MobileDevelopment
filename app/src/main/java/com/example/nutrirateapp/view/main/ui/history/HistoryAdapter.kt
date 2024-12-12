package com.example.nutrirateapp.view.main.ui.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrirateapp.data.model.HistoryItem
import com.example.nutrirateapp.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private val historyList = mutableListOf<HistoryItem>()

    class ViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryItem) {
            binding.apply {
                itemTitle.text = history.productName
                itemGrade.text = history.grade
                itemNama.text = history.name
                itemGrade.setTextColor(getGradeColor(history.grade))
            }
        }

        private fun getGradeColor(grade: String): Int {
            return when (grade) {
                "A" -> Color.parseColor("#008000")
                "B" -> Color.parseColor("#FFD700")
                "C" -> Color.parseColor("#FFA500")
                "D" -> Color.parseColor("#FF4500")
                "E" -> Color.parseColor("#FF0000")
                else -> Color.BLACK
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount() = historyList.size

    fun setData(newHistoryList: List<HistoryItem>) {
        historyList.clear()
        historyList.addAll(newHistoryList)
        notifyDataSetChanged()
    }
}
