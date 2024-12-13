package com.example.nutrirateapp.view.profile

import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.nutrirateapp.databinding.DialogForgotPasswordBinding
import com.example.nutrirateapp.view.login.LoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ForgotPasswordDialog : DialogFragment() {
    private var _binding: DialogForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        _binding = DialogForgotPasswordBinding.inflate(LayoutInflater.from(context))
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
            btnClose.setOnClickListener {
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

            btnSend.setOnClickListener {
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
                        handleResetPassword()
                    }
                    .start()
            }
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.apply {
                etEditName.isEnabled = !isLoading
                btnClose.isEnabled = !isLoading
                btnSend.isEnabled = !isLoading
            }
        }

        viewModel.resetPasswordResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                result.fold(
                    onSuccess = { response ->
                        showSuccessDialog(response.email)
                    },
                    onFailure = { exception ->
                        val errorMessage = when {
                            exception.message?.contains("tidak terdaftar") == true ->
                                "Email tidak terdaftar"
                            exception.message?.contains("tidak valid") == true ->
                                "Format email tidak valid"
                            exception.message?.contains("Terlalu banyak permintaan") == true ->
                                "Terlalu banyak permintaan. Coba lagi nanti"
                            else -> "Gagal mengirim email reset password"
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun showSuccessDialog(email: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Email Terkirim")
            .setMessage("Kami telah mengirim link reset password ke $email. Silakan periksa email Anda.")
            .setPositiveButton("OK") { _, _ ->
                dismiss()
            }
            .show()
    }

    private fun handleResetPassword() {
        val email = binding.etEditName.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(context, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Format email tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.resetPassword(email)
    }

    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onDestroyView() {
        viewModel.resetPasswordResult.removeObservers(this)
        viewModel.isLoading.removeObservers(this)
        _binding = null
        super.onDestroyView()
    }
}