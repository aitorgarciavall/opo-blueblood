package com.example.opoblueblood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC
}

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // Setup

        val bundle:Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        val provider:String? = bundle?.getString("provider")
        setup(email?: "", provider?: "")

        val myLinearLayout = findViewById<LinearLayout>(R.id.testsLinearLayout)
        myLinearLayout.setOnClickListener {
            val intent = Intent(this, TemesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setup(email: String, provider: String){


        val logOutButton: ImageView = findViewById(R.id.signUpButton)


        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

    }
}