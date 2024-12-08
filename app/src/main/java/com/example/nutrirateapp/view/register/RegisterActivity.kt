package com.example.nutrirateapp.view.register

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nutrirateapp.R
import com.example.nutrirateapp.view.camera.CameraActivity
import com.example.nutrirateapp.view.login.LoginActivity
import com.example.nutrirateapp.view.main.MainActivity

class RegisterActivity : AppCompatActivity() {
    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Set up clickable text for "Sudah punya akun"
        val tvSudahPunyaAkun = findViewById<TextView>(R.id.tvSudahPunyaAkun)
        tvSudahPunyaAkun.setOnClickListener {
            // Direct to LoginActivity when "Sudah Punya Akun" is clicked
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Set up clickable text for "disini"
        val descriptionText = findViewById<TextView>(R.id.descriptionText)
        val text = getString(R.string.regis_kitakenalan)
        val spannableString = SpannableString(text)

        // Highlight and make clickable the word "disini"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Direct to LoginActivity when "disini" is clicked
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        // Apply clickable span to the word "disini"
        val start = text.indexOf("disini")
        val end = start + "disini".length
        spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        descriptionText.text = spannableString
        descriptionText.movementMethod = LinkMovementMethod.getInstance() // Make it clickable

    }
}
