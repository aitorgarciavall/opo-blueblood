package com.example.opoblueblood

data class Answer(
    val text: String,
    val isCorrectAnswer: Boolean,
    val hasImage: Boolean = false
)