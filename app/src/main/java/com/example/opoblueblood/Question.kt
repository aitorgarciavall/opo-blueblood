package com.example.opoblueblood

data class Question(
    val questionText: String,
    val answers: List<Answer>,
    val imageResource: Int? = null
) {
    init {
        require(answers.size == 4) { "Debe haber exactamente 4 respuestas" }
        require(answers.count { it.isCorrectAnswer } == 1) { "Debe haber exactamente una respuesta correcta" }
    }

    fun isAnswerCorrect(answerIndex: Int): Boolean = answers[answerIndex].isCorrectAnswer
}