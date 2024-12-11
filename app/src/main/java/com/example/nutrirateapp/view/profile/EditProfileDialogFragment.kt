package com.example.nutrirateapp.view.profile


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.nutrirateapp.R


class EditProfileDialogFragment(private val onSave: (String) -> Unit) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)


        dialog.setContentView(view)


        val window = dialog.window
        if (window != null) {
            val params: WindowManager.LayoutParams = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = params
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.setGravity(Gravity.CENTER)


            // Tambahkan margin 16dp secara programmatically
            window.decorView.setPadding(
                convertDpToPx(16), // Left
                convertDpToPx(16), // Top
                convertDpToPx(16), // Right
                convertDpToPx(16)  // Bottom
            )
        }


        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val etEditName = view.findViewById<EditText>(R.id.etEditName)


        // Handle Cancel Button
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }


        // Handle Save Button
        btnSave.setOnClickListener {
            val newName = etEditName.text.toString()
            if (newName.isNotEmpty()) {
                onSave(newName)
                dialog.dismiss()
            }
        }


        return dialog
    }


    // Helper Function for Converting DP to Pixels
    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}
