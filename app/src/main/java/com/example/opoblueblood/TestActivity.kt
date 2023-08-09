package com.example.opoblueblood

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestActivity : AppCompatActivity() {
    private lateinit var questions: DoublyLinkedList<Question>
    private var currentQuestion: Question? = null
    private lateinit var nomTema: TextView
    private lateinit var questionText: TextView
    private lateinit var answer1: RadioButton
    private lateinit var answer2: RadioButton
    private lateinit var answer3: RadioButton
    private lateinit var answer4: RadioButton
    private lateinit var answerGroup: RadioGroup
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        nomTema = findViewById(R.id.nomTema)

        // Declarando checkButton como una variable local en onCreate
        val temaNombre = intent.getStringExtra("tema_nombre") ?: "Error -> Nombre de tema"
        nomTema.text = temaNombre
        questionText = findViewById(R.id.question_text)
        answerGroup = findViewById(R.id.answer_group)
        answer1 = findViewById(R.id.answer_1)
        answer2 = findViewById(R.id.answer_2)
        answer3 = findViewById(R.id.answer_3)
        answer4 = findViewById(R.id.answer_4)
        val nextButton: Button = findViewById(R.id.next_button)
        val checkButton: Button = findViewById(R.id.check_button)
        resultText = findViewById(R.id.result_text)

        nextButton.setOnClickListener {
            currentQuestion = questions.removeFirst()
            displayQuestion(currentQuestion)
            resultText.text = ""
            checkButton.isEnabled = true // Habilita el botón "check" nuevamente
            answerGroup.clearCheck() // Limpia la selección en los radio buttons

            // Establecer isSelected a false para cada RadioButton
            answer1.isSelected = false
            answer2.isSelected = false
            answer3.isSelected = false
            answer4.isSelected = false

            // Deshabilita los RadioButtons
            answer1.isEnabled = true
            answer2.isEnabled = true
            answer3.isEnabled = true
            answer4.isEnabled = true
        }

        checkButton.setOnClickListener {
            val selectedAnswerIndex = when {
                answer1.isChecked -> 0
                answer2.isChecked -> 1
                answer3.isChecked -> 2
                answer4.isChecked -> 3
                else -> -1
            }

            val selectedRadioButton = when (selectedAnswerIndex) {
                0 -> answer1
                1 -> answer2
                2 -> answer3
                3 -> answer4
                else -> null
            }

            if (currentQuestion?.isAnswerCorrect(selectedAnswerIndex) == true) {
                resultText.text = "Correcte!"
                selectedRadioButton?.isChecked = true
                checkButton.isEnabled = false // Deshabilita el botón

                // Deshabilita los RadioButtons
                answer1.isEnabled = false
                answer2.isEnabled = false
                answer3.isEnabled = false
                answer4.isEnabled = false
            } else {
                resultText.text = "Incorrecte!"
                selectedRadioButton?.isChecked = true
                selectedRadioButton?.isSelected = true // Esto activará el estado rojo
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://opoblueblood.000webhostapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(Api::class.java)

        val call = api.getQuestions(temaNombre)
        call.enqueue(object : Callback<QuizResponse> {
            override fun onResponse(call: Call<QuizResponse>, response: Response<QuizResponse>) {
                val body = response.body()
                if (body != null) {
                    questions = DoublyLinkedList<Question>()
                    for (questionResponse in body.preguntas) {
                        val question = Question(
                            questionText = questionResponse.texto,
                            answers = questionResponse.opciones,
                            correctAnswerIndex = questionResponse.respuestaCorrecta.toInt(),
                            imageResource = null // Aquí puedes agregar la lógica para manejar la imagen si la tienes
                        )
                        questions.addLast(question)
                    }
                    currentQuestion = questions.removeFirst()
                    displayQuestion(currentQuestion)
                }
            }

            override fun onFailure(call: Call<QuizResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun displayQuestion(question: Question?) {
        if (question != null) {
            questionText.text = question.questionText
            answer1.text = question.answers[0]
            answer2.text = question.answers[1]
            answer3.text = question.answers[2]
            answer4.text = question.answers[3]
            answerGroup.clearCheck()
        } else {
            // Aquí puedes manejar el caso cuando noPerdón por el corte. Aquí está el código completo:

        }
    }
}
