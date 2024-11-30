package com.example.nutrirateapp.view.result

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrirateapp.R

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)

        // Ambil data dari Intent
        val protein = intent.getStringExtra("PROTEIN") ?: "Not found"
        val energy = intent.getStringExtra("ENERGY") ?: "Not found"
        val fat = intent.getStringExtra("FAT") ?: "Not found"
        val saturatedFat = intent.getStringExtra("SATURATED_FAT") ?: "Not found"
        val sugar = intent.getStringExtra("SUGAR") ?: "Not found"
        val fiber = intent.getStringExtra("FIBER") ?: "Not found"
        val sodium = intent.getStringExtra("SODIUM") ?: "Not found"

        // Pastikan ID sesuai dengan EditText di layout
        findViewById<EditText>(R.id.proteinEditText).setText(protein)
        findViewById<EditText>(R.id.energyEditText).setText(energy)
        findViewById<EditText>(R.id.fatEditText).setText(fat)
        findViewById<EditText>(R.id.saturatedFatEditText).setText(saturatedFat)
        findViewById<EditText>(R.id.sugarEditText).setText(sugar)
        findViewById<EditText>(R.id.fiberEditText).setText(fiber)
        findViewById<EditText>(R.id.sodiumEditText).setText(sodium)
    }
}