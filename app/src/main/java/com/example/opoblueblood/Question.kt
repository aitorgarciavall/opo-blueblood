package com.example.opoblueblood

data class Question(
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val imageResource: Int? = null
) {
    init {
        require(answers.size == 4) { "Debe haber exactamente 4 respuestas" }
        require(correctAnswerIndex in 0..3) { "Índice de respuesta correcta inválido" }
    }

    fun isAnswerCorrect(answerIndex: Int): Boolean = answerIndex == correctAnswerIndex
}
