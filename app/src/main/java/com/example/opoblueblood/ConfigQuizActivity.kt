package com.example.opoblueblood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ConfigQuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_quiz)

        val temaNombre = intent.getStringExtra("tema_nombre")
        // Utiliza el temaNombre como desees, por ejemplo, muestra el nombre en un TextView.
    }
}