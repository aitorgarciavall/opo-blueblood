package com.example.opoblueblood

data class QuizResponse(
    val id: String?,
    val nombre: String?,
    val tema: String?,
    val preguntas: List<QuestionResponse>
)
