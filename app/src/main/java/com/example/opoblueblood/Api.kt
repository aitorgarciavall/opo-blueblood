package com.example.opoblueblood

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/")
    fun getQuestions(@Query("tema") tema: String): Call<QuizResponse>
}
