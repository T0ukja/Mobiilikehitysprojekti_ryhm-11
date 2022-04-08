package com.example.mymaps

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val showBtn: ImageView = findViewById(R.id.show)
        val pwd: TextView = findViewById(R.id.password)
        val changePwd: Button = findViewById(R.id.change_password)
        val ok: Button = findViewById(R.id.ok)
        var show: Boolean = false
        showBtn.setOnClickListener {
            if (show) {
                pwd.transformationMethod = PasswordTransformationMethod.getInstance()
                show = false
            } else {
                pwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                show = true
            }
        }
        changePwd.setOnClickListener {
            this.findViewById<LinearLayout>(R.id.new_password).visibility = View.VISIBLE
            this.findViewById<LinearLayout>(R.id.repeat_new_password).visibility = View.VISIBLE
            ok.visibility = View.VISIBLE
            changePwd.visibility = View.GONE
        }
        ok.setOnClickListener {
            val newPswrd: String =
                this.findViewById<EditText>(R.id.new_password_text).text.toString()
            if (newPswrd == this.findViewById<EditText>(R.id.repeat_new_password_text).text.toString()) {
                if (newPswrd == "") {
                    Toast.makeText(
                        applicationContext,
                        "Salasana ei voi olla tyhjä",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    changePassword(newPswrd)
                }
            } else {
                Toast.makeText(applicationContext, "Salasanat eivät täsmänneet", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun changePassword(newPassword: String) {
        this.findViewById<TextView>(R.id.password).text = newPassword
        this.findViewById<LinearLayout>(R.id.new_password).visibility = View.GONE
        this.findViewById<LinearLayout>(R.id.repeat_new_password).visibility = View.GONE
        this.findViewById<Button>(R.id.ok).visibility = View.GONE
        this.findViewById<Button>(R.id.change_password).visibility = View.VISIBLE
        this.findViewById<EditText>(R.id.new_password_text).text.clear()
        this.findViewById<EditText>(R.id.repeat_new_password_text).text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.profile)?.setVisible(false)
        menu?.findItem(R.id.icLogin)?.setVisible(false)
        return super.onPrepareOptionsMenu(menu)
    }


}