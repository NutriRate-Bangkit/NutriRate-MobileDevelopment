package com.example.nutrirateapp.Onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nutrirateapp.MainActivity
import com.example.nutrirateapp.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    // ViewBinding untuk Fragment
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(imageRes: Int, title: String, description: String, isLast: Boolean): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putInt("imageRes", imageRes)
            args.putString("title", title)
            args.putString("description", description)
            args.putBoolean("isLast", isLast)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi ViewBinding
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageRes = arguments?.getInt("imageRes") ?: 0
        val title = arguments?.getString("title") ?: ""
        val description = arguments?.getString("description") ?: ""
        val isLast = arguments?.getBoolean("isLast") ?: false

        binding.image.setImageResource(imageRes)
        binding.title.text = title
        binding.description.text = description

        binding.startButton.visibility = if (isLast) View.VISIBLE else View.GONE
        binding.startButton.setOnClickListener {

            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Bersihkan binding untuk mencegah memory leak
        _binding = null
    }
}
