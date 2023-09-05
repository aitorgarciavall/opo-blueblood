    package com.example.opoblueblood


    data class QuestionResponse(
        val id: String,
        val texto: String,
        val tema: String,
        val opciones: List<Answer>,
        val respuestaCorrecta: String
    )
