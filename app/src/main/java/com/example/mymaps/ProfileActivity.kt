package com.example.mymaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //adds items to the action bar
        menuInflater.inflate(R.menu.top_bar_menu_logged_in, menu)
        return true
    }
}