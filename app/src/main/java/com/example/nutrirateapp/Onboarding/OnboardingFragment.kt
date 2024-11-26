package com.example.nutrirateapp.Onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nutrirateapp.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    // ViewBinding untuk fragment
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
        // Inisialisasi ViewBinding
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari arguments
        val imageRes = arguments?.getInt("imageRes") ?: 0
        val subtitle = arguments?.getString("subtitle") ?: ""
        val description = arguments?.getString("description") ?: ""
        val isLast = arguments?.getBoolean("isLast") ?: false

        // Tetap tampilkan judul NutriRate di bagian atas
        binding.title.text = "NutriRate"

        // Set subtitle dan deskripsi sesuai slide
        binding.subtitle.text = subtitle
        binding.description.text = description

        // Set gambar
        binding.image.setImageResource(imageRes)

        // Tampilkan tombol "Let's Start!" hanya di slide terakhir
        binding.startButton.visibility = if (isLast) View.VISIBLE else View.GONE
        binding.startButton.setOnClickListener {
            // Pindah ke MainActivity atau langkah berikutnya
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Bersihkan binding untuk mencegah memory leak
        _binding = null
    }
}
