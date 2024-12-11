package com.example.nutrirateapp.view.profile


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.nutrirateapp.R


class EmailSentDialogFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_email_sent, null)


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


        val btnClose = view.findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            dialog.dismiss() // Close the dialog
        }


        return dialog
    }


    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}
