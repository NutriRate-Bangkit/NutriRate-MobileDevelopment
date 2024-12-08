package com.example.nutrirateapp.view.Onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nutrirateapp.databinding.FragmentOnboardingBinding
import com.example.nutrirateapp.view.register.RegisterActivity

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(imageRes: Int, subtitle: String, description: String, isLast: Boolean): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putInt("imageRes", imageRes)
            args.putString("subtitle", subtitle)
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
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageRes = arguments?.getInt("imageRes") ?: 0
        val subtitle = arguments?.getString("subtitle") ?: ""
        val description = arguments?.getString("description") ?: ""
        val isLast = arguments?.getBoolean("isLast") ?: false


        binding.subtitle.text = subtitle
        binding.description.text = description

        binding.image.setImageResource(imageRes)

        binding.startButton.visibility = if (isLast) View.VISIBLE else View.GONE
        binding.startButton.setOnClickListener {
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
