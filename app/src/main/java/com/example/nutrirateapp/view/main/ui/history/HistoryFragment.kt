package com.example.nutrirateapp.view.main.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutrirateapp.R
import com.example.nutrirateapp.databinding.FragmentHistoryBinding

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
        binding.storyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.storyRecyclerView.adapter = HistoryAdapter(
            listOf(
                HistoryItem(R.drawable.ic_salad, "Salad", "Grade A"),
                HistoryItem(R.drawable.ic_salad, "Vegetable Bowl", "Grade B"),
                HistoryItem(R.drawable.ic_salad, "Fruit Mix", "Grade A")
            )
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
