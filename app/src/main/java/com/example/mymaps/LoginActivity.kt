package com.example.mymaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val ivHome = findViewById<ImageView>(R.id.ivHome)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnSignIn.setOnClickListener() {
            if (etUsername.text.trim().isNotEmpty() && etPassword.text.isNotEmpty()) {
                Toast.makeText(this, "Kirjaudutaan sisään...", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "Täytä kaikki kohdat", Toast.LENGTH_LONG).show()
            }
        }

        tvRegister.setOnClickListener() {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        ivHome.setOnClickListener() {
            finish()
        }

    }

}