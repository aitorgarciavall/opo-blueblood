package com.example.opoblueblood

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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
    private lateinit var EditTextPosition: EditText
    private lateinit var TotalQuestions: EditText
    private lateinit var loadingProgress: ProgressBar
    var position = 1
    private val userAnswers = mutableMapOf<Question, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        nomTema = findViewById(R.id.nomTema)


        val temaNombre = intent.getStringExtra("tema_nombre") ?: "Error -> Nombre de tema enviado"
        val questionCount = intent.getIntExtra("question_count", 0) ?: "Error -> Nº de preguntas enviadas"

        nomTema.text = temaNombre
        questionText = findViewById(R.id.question_text)
        answerGroup = findViewById(R.id.answer_group)
        answer1 = findViewById(R.id.answer_1)
        answer2 = findViewById(R.id.answer_2)
        answer3 = findViewById(R.id.answer_3)
        answer4 = findViewById(R.id.answer_4)
        EditTextPosition = findViewById(R.id.actualPosition)
        TotalQuestions = findViewById(R.id.totalQuest)
        val nextButton: Button = findViewById(R.id.next_button)
        val previousButton: Button = findViewById(R.id.previous_button)
        previousButton.isEnabled = false
        previousButton.visibility = View.INVISIBLE
        nextButton.isEnabled = false  // Deshabilita el botón al inicio
        val checkButton: Button = findViewById(R.id.check_button)
        resultText = findViewById(R.id.result_text)
        loadingProgress = findViewById(R.id.loading_progress)
        EditTextPosition.setText(position.toString())
        TotalQuestions.setText(questionCount.toString())


        previousButton.setOnClickListener {
            currentQuestion = questions.getPrevious()
            displayQuestion(currentQuestion)
            // Actualiza la posición y otros elementos de la UI si es necesario
            position -= 1
            EditTextPosition.setText(position.toString())

            if (position == 1){
                previousButton.isEnabled = false
                previousButton.visibility = View.INVISIBLE
            }else{
                previousButton.isEnabled = true
                previousButton.visibility = View.VISIBLE
            }

            if (position == questionCount){
                nextButton.isEnabled = false
                nextButton.visibility = View.INVISIBLE
            }else{
                nextButton.isEnabled = true
                nextButton.visibility = View.VISIBLE
            }
        }

        nextButton.setOnClickListener {
            //currentQuestion = questions.removeFirst()
            currentQuestion = questions.getNext()
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

            // Actualizar la posición
            position += 1
            EditTextPosition.setText(position.toString())

            if (position == questionCount){
                nextButton.isEnabled = false
                nextButton.visibility = View.INVISIBLE
            }else{
                nextButton.isEnabled = true
                nextButton.visibility = View.VISIBLE
            }

            if (position == 1){
                previousButton.isEnabled = false
                previousButton.visibility = View.INVISIBLE
            }else{
                previousButton.isEnabled = true
                previousButton.visibility = View.VISIBLE
            }
        }

        checkButton.setOnClickListener {
            val selectedAnswerIndex = when {
                answer1.isChecked -> 0
                answer2.isChecked -> 1
                answer3.isChecked -> 2
                answer4.isChecked -> 3
                else -> -1
            }

            currentQuestion?.let {
                userAnswers[it] = selectedAnswerIndex
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

        Log.d("API_CALL", "Haciendo llamada a la API...")
        val call = api.getQuestions(temaNombre)
        call.enqueue(object : Callback<QuizResponse> {
            override fun onResponse(call: Call<QuizResponse>, response: Response<QuizResponse>) {
                val body = response.body()
                Log.d("API_CALL", "Respuesta recibida: ${response.body()}")
                if (body != null) {
                    questions = DoublyLinkedList<Question>()
                    for (questionResponse in body.preguntas.shuffled().take(questionCount as Int)) {
                        val answersList = questionResponse.opciones.map { option ->
                            Answer(
                                text = option.text,
                                isCorrectAnswer = option.isCorrectAnswer,
                                hasImage = option.hasImage
                            )
                        }
                        val question = Question(
                            questionText = questionResponse.texto,
                            answers = answersList,
                            imageResource = null // Aquí puedes agregar la lógica para manejar la imagen si la tienes
                        )
                        questions.addLast(question)
                    }

                    currentQuestion = questions.getFirst()
                    displayQuestion(currentQuestion)

                    nextButton.isEnabled = true  // Activa el botón después de inicializar questions
                    loadingProgress.visibility = View.GONE  // Oculta el ProgressBar
                }
            }

            override fun onFailure(call: Call<QuizResponse>, t: Throwable) {
                Log.d("API_CALL", "Fallo en la llamada: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    private fun displayQuestion(question: Question?) {
        question?.let {
            questionText.text = it.questionText
            answer1.text = it.answers[0].text
            answer2.text = it.answers[1].text
            answer3.text = it.answers[2].text
            answer4.text = it.answers[3].text
            answerGroup.clearCheck()

            userAnswers[it]?.let { selectedAnswer ->
                when (selectedAnswer) {
                    0 -> answer1.isChecked = true
                    1 -> answer2.isChecked = true
                    2 -> answer3.isChecked = true
                    3 -> answer4.isChecked = true
                }
            }
        }
    }
}
