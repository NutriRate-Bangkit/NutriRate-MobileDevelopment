package com.example.nutrirateapp.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrirateapp.R
import com.example.nutrirateapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Menangani klik pada "Belum punya akun?"
        val tvBelumPunyaAkun = findViewById<TextView>(R.id.tvBelumPunyaAkun)
        tvBelumPunyaAkun.setOnClickListener {
            // Arahkan ke RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
