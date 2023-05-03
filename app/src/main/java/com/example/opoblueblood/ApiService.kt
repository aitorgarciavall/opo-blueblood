package com.example.opoblueblood

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("temesJSON.php")
    fun getTemes(): Call<TemesResponse>
}
