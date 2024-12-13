package com.example.nutrirateapp.view.main.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutrirateapp.databinding.FragmentHistoryBinding
import com.example.nutrirateapp.view.detail.DetailHistoryActivity

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        viewModel.getHistory()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter().apply {
            setOnItemClickCallback { historyItem ->
                // Navigate ke DetailHistoryActivity dengan data history
                val intent = Intent(requireContext(), DetailHistoryActivity::class.java).apply {
                    putExtra("HISTORY_ITEM", historyItem)
                }
                startActivity(intent)
            }
        }

        binding.storyRecyclerView.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.historyResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { response ->
                    historyAdapter.setData(response.history)
                },
                onFailure = { exception ->
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

