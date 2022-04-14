package com.example.mymaps

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ProfileActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    val user = auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val showBtn: ImageView = findViewById(R.id.show)
        val pwd: TextView = findViewById(R.id.new_password_text)
        val pwd2: TextView = findViewById(R.id.repeat_new_password_text)
        val changePwd: Button = findViewById(R.id.change_password)
        val ok: Button = findViewById(R.id.ok)
        var show = true
        val usernamePA = findViewById<TextView>(R.id.username)
        val Email = user?.email
        usernamePA.text = Email.toString()
        showBtn.setOnClickListener {
            if (show) {
                pwd.transformationMethod = PasswordTransformationMethod.getInstance()
                pwd2.transformationMethod = PasswordTransformationMethod.getInstance()
                show = false
            } else {
                pwd2.transformationMethod = HideReturnsTransformationMethod.getInstance()
                pwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                show = true
            }
        }
        changePwd.setOnClickListener {
            this.findViewById<LinearLayout>(R.id.new_password).visibility = View.VISIBLE
            this.findViewById<LinearLayout>(R.id.repeat_new_password).visibility = View.VISIBLE
            this.findViewById<ImageView>(R.id.show).visibility = View.VISIBLE
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
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    changePassword(newPswrd)
                }
            } else {
                Toast.makeText(applicationContext, "Salasanat eivät täsmänneet", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun changePassword(newPassword: String) {
        user!!.updatePassword(this.findViewById<EditText>(R.id.repeat_new_password_text).text.toString())
            .addOnCompleteListener { task ->
                if (task.isComplete) {
                    Toast.makeText(
                        applicationContext,
                        "Salasana vaihdettu onnistuneesti",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Salasanan vaihto ei onnistunut",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        this.findViewById<TextView>(R.id.new_password_text).text = newPassword
        this.findViewById<LinearLayout>(R.id.new_password).visibility = View.GONE
        this.findViewById<LinearLayout>(R.id.repeat_new_password).visibility = View.GONE
        this.findViewById<Button>(R.id.ok).visibility = View.GONE
        this.findViewById<Button>(R.id.change_password).visibility = View.VISIBLE
        this.findViewById<ImageView>(R.id.show).visibility = View.GONE
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

/*    override fun onBackPressed() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.icLogOut -> {
                val intent = Intent(this, MapsActivity::class.java)
                auth.signOut()
                startActivity(intent)
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}