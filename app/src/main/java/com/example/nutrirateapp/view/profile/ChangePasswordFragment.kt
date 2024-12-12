package com.example.nutrirateapp.view.profile


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.nutrirateapp.R


class ChangePasswordFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_change_password, null)

        dialog.setContentView(view)

        val window = dialog.window
        if (window != null) {
            val params: WindowManager.LayoutParams = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = params
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.setGravity(Gravity.CENTER)

            window.decorView.setPadding(
                convertDpToPx(16), // Left
                convertDpToPx(16), // Top
                convertDpToPx(16), // Right
                convertDpToPx(16)  // Bottom
            )
        }

        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val etCurrentPassword = view.findViewById<EditText>(R.id.et_current_password)
        val etNewPassword = view.findViewById<EditText>(R.id.et_new_password)
        val etRepeatPassword = view.findViewById<EditText>(R.id.et_repeat_password)

        btnCancel.setOnClickListener {
            dialog.dismiss() // Close the dialog
        }

        btnSave.setOnClickListener {
            val currentPassword = etCurrentPassword.text.toString()
            val newPassword = etNewPassword.text.toString()
            val repeatPassword = etRepeatPassword.text.toString()

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
                    // All validations passed
                    Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss() // Close the dialog and return to profile page
                }
            }
        }

        return dialog
    }

    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}

