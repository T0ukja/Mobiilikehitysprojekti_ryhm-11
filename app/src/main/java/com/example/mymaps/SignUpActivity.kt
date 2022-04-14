package com.example.mymaps

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.view.Menu

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val etUsernameSU = findViewById<EditText>(R.id.etUsernameSU)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPasswordSU = findViewById<EditText>(R.id.etPasswordSU)
        val etCPasswordSU = findViewById<EditText>(R.id.etCPasswordSU)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvSignIn = findViewById<TextView>(R.id.tvSignIn)


        auth = Firebase.auth

        btnSignUp.setOnClickListener() {
            if (etUsernameSU.text.trim().isNotEmpty() && etPasswordSU.text.isNotEmpty() &&
                etEmail.text.trim().isNotEmpty() && etCPasswordSU.text.isNotEmpty()
            ) {
                if (etPasswordSU.text.toString() == etCPasswordSU.text.toString()) {
                    val email = etEmail.text.toString().trim()
                    val password = etPasswordSU.text.toString().trim()
                    createUser(email, password)
                    Toast.makeText(this, "Luodaan profiili...", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Salasanat eivät täsmää", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Täytä kaikki kohdat", Toast.LENGTH_LONG).show()
            }
        }


        tvSignIn.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    Toast.makeText(this, "Rekisteröinti onnistui", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.icLogin)?.setVisible(false)
        menu?.findItem(R.id.profile)?.setVisible(false)
        menu?.findItem(R.id.icLogOut)?.setVisible(false)
        return super.onPrepareOptionsMenu(menu)
    }
}