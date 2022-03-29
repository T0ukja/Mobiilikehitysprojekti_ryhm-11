package com.example.mymaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val etUsernameSU = findViewById<EditText>(R.id.etUsernameSU)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPasswordSU = findViewById<EditText>(R.id.etPasswordSU)
        val etCPasswordSU = findViewById<EditText>(R.id.etCPasswordSU)

        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        val tvSignIn = findViewById<TextView>(R.id.tvSignIn)

        val ivHomeSU = findViewById<ImageView>(R.id.ivHomeSU)

        btnSignUp.setOnClickListener() {
            if (etUsernameSU.text.trim().isNotEmpty() && etPasswordSU.text.isNotEmpty() &&
                etEmail.text.trim().isNotEmpty() && etCPasswordSU.text.isNotEmpty()) {
                if (etPasswordSU.text.toString() == etCPasswordSU.text.toString()) {
                    Toast.makeText(this, "Luodaan profiili...", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this, "Salasanat eivät täsmää", Toast.LENGTH_LONG).show()
                }
            }
            else {
                Toast.makeText(this, "Täytä kaikki kohdat", Toast.LENGTH_LONG).show()
            }
        }


        tvSignIn.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        ivHomeSU.setOnClickListener() {
            finish()
        }
    }
}