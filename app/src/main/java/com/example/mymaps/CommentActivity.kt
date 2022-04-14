package com.example.mymaps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymaps.adapters.ViewPagerAdapter
import com.example.mymaps.databinding.ActivityCommentBinding


class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_comment)
        setContentView(view)

        setUpTabs()
    }

    private fun setUpTabs() {

        val adapter = ViewPagerAdapter(supportFragmentManager)

        val markerdata = intent.getStringExtra("Ravintola")
      //  binding.moro.setText(moro)
        adapter.addFragment(BarFrag1(), markerdata.toString())
        adapter.addFragment(BarFrag2(), "Tapahtumat")
        adapter.addFragment(BarFrag3(), "Kommentit")
        binding.viewPager.adapter = adapter
        binding.bartablayout.setupWithViewPager(binding.viewPager)


    }

}