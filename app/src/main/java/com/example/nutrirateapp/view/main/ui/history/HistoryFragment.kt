package com.example.nutrirateapp.view.main.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.FragmentHistoryBinding
import com.example.nutrirateapp.view.detail.DetailHistoryActivity

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        val historyItems = listOf(
            HistoryItem("CIKI RING", "B", "Ujang"),
            HistoryItem("COKLAT", "C", "Agus"),
            HistoryItem("SILVERQUEEN", "A", "Zare")
        )

        binding.storyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.storyRecyclerView.adapter = HistoryAdapter(historyItems) { selectedItem ->
            val intent = Intent(requireContext(), DetailHistoryActivity::class.java).apply {
                putExtra("PRODUCT_NAME", selectedItem.title)
                putExtra("GRADE", selectedItem.grade)
                putExtra("USER", selectedItem.user)
            }
            startActivity(intent)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

