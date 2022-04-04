package com.example.mymaps

import android.graphics.Color
import android.graphics.ColorSpace
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.provider.CalendarContract
import android.view.Gravity
import android.view.Menu
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import java.lang.reflect.Array

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var list = arrayOf("keijo", "ismo", "lauri")
        updateGrid(list)
        updateInfo(list.size.toString())
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


    // Päivitetään käyttäjän "kupongit"
    private fun updateGrid(list: kotlin.Array<String>) {
        for (i in list.indices) {
            val button = Button(this)
            button.setBackgroundColor(getColor(R.color.primary))
            button.setText(list[i])
            button.height = 200
            button.width = 500
            button.gravity = Gravity.CENTER
            findViewById<GridLayout>(R.id.grid).addView(button)
        }
    }

    // Päivitetään käyttäjän info
    private fun updateInfo(quantity: String) {
        val textView = findViewById<TextView>(R.id.coupon_quantity)
        textView.text = quantity
    }
}