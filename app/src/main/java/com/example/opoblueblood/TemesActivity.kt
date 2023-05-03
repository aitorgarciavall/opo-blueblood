package com.example.opoblueblood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.GET

class TemesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TemaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temes)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://opoblueblood.000webhostapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getTemes()

        call.enqueue(object : Callback<TemesResponse> {
            override fun onResponse(call: Call<TemesResponse>, response: Response<TemesResponse>) {
                if (response.isSuccessful) {
                    val temesResponse = response.body()
                    temesResponse?.let {
                        adapter = TemaAdapter(it.temes) { tema ->
                            val intent = Intent(this@TemesActivity, ConfigQuizActivity::class.java)
                            intent.putExtra("tema_nombre", tema.nom)
                            startActivity(intent)
                        }
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("TemesActivity", "Error al obtener los datos: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TemesResponse>, t: Throwable) {
                Log.e("TemesActivity", "Error al llamar a la API", t)
            }
        })
    }
}