package com.example.mymaps

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mymaps.adapters.ViewPagerAdapter
import com.example.mymaps.databinding.ActivityCommentBinding
import com.google.firebase.database.DatabaseReference

class CommentActivity : AppCompatActivity() {

    var markerdata: String = ""
    private lateinit var binding: ActivityCommentBinding
    lateinit var ref: DatabaseReference


    data class kommentti(
        val arvosana: Double = 0.0,
        val palaute: String = ""
    )


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)

        val view = binding.root


        setContentView(view)





        setUpTabs()


    }


    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        val loggedIn = intent.extras!!.getBoolean("IsLoggedInData")
        val markerdata = intent.getStringExtra("Ravintola")
        var teksti = findViewById<TextView>(R.id.barname)
        teksti.setText(markerdata)
        Log.d("Kirjautumistieto:", loggedIn.toString())
        val bundle = Bundle()
        bundle.putString("key", markerdata)

        adapter.addFragment(BarFrag1(markerdata), "Tarjoukset")
        adapter.addFragment(BarFrag2(markerdata), "Tapahtumat")
        adapter.addFragment(BarFrag3(markerdata, loggedIn), "Kommentit")





        binding.viewPager.adapter = adapter
        binding.bartablayout.setupWithViewPager(binding.viewPager)


    }

}