package com.example.mymaps

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val cbShowPW = findViewById<CheckBox>(R.id.cbShowPW)

        auth = Firebase.auth

        btnSignIn.setOnClickListener() {
            if (etEmail.text.trim().isNotEmpty() && etPassword.text.isNotEmpty()) {
                Toast.makeText(this, "Kirjaudutaan sisään...", Toast.LENGTH_LONG).show()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                singIn(email, password)
            } else {
                Toast.makeText(this, "Täytä kaikki kohdat", Toast.LENGTH_LONG).show()
            }
        }

        tvRegister.setOnClickListener() {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }


        cbShowPW.setOnClickListener() {
            if (cbShowPW.isChecked) {
                etPassword.inputType = 1
            } else
                etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun singIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    val siEmail = user?.email
                    Toast.makeText(
                        this,
                        "Sisäänkirjauduttu spostilla " + siEmail.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, MapsActivity::class.java).putExtra("sadasd", false)
                        .putExtra("loggedIn", true)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Sisäänkirjautuminen epäonnistui.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       // menuInflater.inflate(R.menu.top_bar_menu, menu)
       // return super.onCreateOptionsMenu(menu)

        //adds items to the action bar
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.icLogin)?.setVisible(false)
        menu?.findItem(R.id.profile)?.setVisible(false)
        menu?.findItem(R.id.icLogOut)?.setVisible(false)
        return super.onPrepareOptionsMenu(menu)
    }

}