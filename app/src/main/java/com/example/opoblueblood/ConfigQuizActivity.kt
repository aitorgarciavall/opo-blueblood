package com.example.opoblueblood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.RadioGroup
import android.widget.SeekBar

class ConfigQuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_quiz)

        val temaNombre = intent.getStringExtra("tema_nombre")
        // Utiliza el temaNombre como desees, por ejemplo, muestra el nombre en un TextView.

        val button = findViewById<Button>(R.id.start_quiz_button)
        button.setOnClickListener {
            val selectedId = findViewById<RadioGroup>(R.id.question_count_radio_group).checkedRadioButtonId
            val questionCount = when (selectedId) {
                R.id.question_count_10 -> 10
                R.id.question_count_20 -> 20
                R.id.question_count_30 -> 30
                else -> 10
            }
            val intent = Intent(this, TestActivity::class.java)
            intent.putExtra("question_count", questionCount)
            intent.putExtra("tema_nombre", temaNombre)
            startActivity(intent)
        }
    }

    fun showQuestionCountDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Configuración de preguntas")
        builder.setMessage("Elija el número de preguntas para el test")

        val view = layoutInflater.inflate(R.layout.activity_config_quiz, null)
        builder.setView(view)
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
}
