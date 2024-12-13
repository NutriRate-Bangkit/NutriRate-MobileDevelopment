package com.example.nutrirateapp.view.profile


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.nutrirateapp.databinding.DialogChangePasswordBinding


class ChangePasswordFragment : DialogFragment() {
    private var _binding: DialogChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        _binding = DialogChangePasswordBinding.inflate(LayoutInflater.from(context))
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
                dismiss()
            }

            btnSave.setOnClickListener {
                handlePasswordChange()
            }
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnSave.isEnabled = !isLoading
            binding.btnCancel.isEnabled = !isLoading
        }

        viewModel.changePasswordResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                result.fold(
                    onSuccess = { response ->
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                        dismiss()
                    },
                    onFailure = { exception ->
                        val errorMessage = when {
                            exception.message?.contains("auth/wrong-password") == true ->
                                "Password saat ini salah"
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

    private fun handlePasswordChange() {
        val currentPassword = binding.etCurrentPassword.text.toString()
        val newPassword = binding.etNewPassword.text.toString()
        val repeatPassword = binding.etRepeatPassword.text.toString()

        when {
            currentPassword.isEmpty() -> {
                Toast.makeText(context, "Current password cannot be empty", Toast.LENGTH_SHORT).show()
            }
            newPassword.isEmpty() || repeatPassword.isEmpty() -> {
                Toast.makeText(context, "New password fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
            currentPassword == newPassword -> {
                Toast.makeText(context, "New password cannot be the same as the current password", Toast.LENGTH_SHORT).show()
            }
            newPassword != repeatPassword -> {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
            else -> {
                viewModel.changePassword(currentPassword, newPassword)
            }
        }
    }

    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onDestroyView() {
        viewModel.changePasswordResult.removeObservers(this)
        viewModel.isLoading.removeObservers(this)
        _binding = null
        super.onDestroyView()
    }
}