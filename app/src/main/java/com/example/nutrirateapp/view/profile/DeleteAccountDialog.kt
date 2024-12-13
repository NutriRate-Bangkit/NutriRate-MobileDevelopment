package com.example.nutrirateapp.view.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.nutrirateapp.databinding.DialogConfirmDeleteBinding
import com.example.nutrirateapp.view.login.LoginActivity

class DeleteAccountDialog : DialogFragment() {
    private var _binding: DialogConfirmDeleteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        _binding = DialogConfirmDeleteBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        setupWindowAttributes(dialog)
        setupListeners()
        setupObservers()

        return dialog
    }

    private fun setupWindowAttributes(dialog: Dialog) {
        dialog.window?.apply {
            val params: WindowManager.LayoutParams = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
            setBackgroundDrawableResource(android.R.color.transparent)
            setGravity(Gravity.CENTER)

            decorView.setPadding(
                convertDpToPx(16),
                convertDpToPx(16),
                convertDpToPx(16),
                convertDpToPx(16)
            )
        }
    }

    private fun setupListeners() {
        with(binding) {
            btnCancel.setOnClickListener {
                it.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                        dismiss()
                    }
                    .start()
            }

            btnDelete.setOnClickListener {
                it.animate()
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()

                        deleteAccount()
                    }
                    .start()
            }
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnDelete.isEnabled = !isLoading
            binding.btnCancel.isEnabled = !isLoading
        }

        viewModel.deleteAccountResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                result.fold(
                    onSuccess = { response ->
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    },
                    onFailure = { exception ->
                        val errorMessage = when {
                            exception.message?.contains("Token") == true ->
                                "Sesi telah berakhir"
                            else -> "Terjadi kesalahan pada server"
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun deleteAccount() {
        viewModel.deleteAccount()
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onDestroyView() {
        viewModel.deleteAccountResult.removeObservers(this)
        viewModel.isLoading.removeObservers(this)
        _binding = null
        super.onDestroyView()
    }
}