package com.example.mymaps

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymaps.adapters.ViewPagerAdapter
import com.example.mymaps.databinding.ActivityCommentBinding
import com.google.firebase.database.DatabaseReference

class CommentActivity : AppCompatActivity() {

    var markerdata: String = ""
    private lateinit var binding: ActivityCommentBinding
    lateinit var ref: DatabaseReference
    //  lateinit var listView: ListView
    lateinit var kommenttiteksti: String
    var arvosanatahti: Double = 0.0

    data class RestaurantFeedItem(
        val title: String = "",
        val description: String = ""
    )


    data class kommentti(
        val arvosana: Double = 0.0,
        val palaute: String = ""
    )


    override fun onCreate(savedInstanceState: Bundle?) {

        markerdata = intent.getStringExtra("Ravintola")!!

        //  markerdata = intent.getStringExtra("Ravintola")!!
        ref = FirebaseDatabase.getInstance().getReference("Ravintolat").child(markerdata)
            .child("Kommentit")
        //productList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {


//            fun ajetaanarvo(){
//                kommenttiteksti = "Ihan jees, tää tuli ohjelmasta"
//                arvosanatahti = 3.5
//                val productId: String? = ref.push().key
//                val item = kommentti(arvosanatahti, kommenttiteksti)
//
//                ref.child(productId.toString()).setValue(item).addOnCompleteListener{
//                    Toast.makeText(applicationContext, "Kommentti jätetty", Toast.LENGTH_LONG).show()
//                }
//            }


            override fun onCancelled(error: DatabaseError) {



        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)


            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    //       productList.clear()
                    Log.d("SNAPSHOT", snapshot.value.toString())
                    Log.d("SssssNAPSHOT", markerdata)
                    Log.d("markerdata", markerdata)



        setContentView(view)


                    val RestaurantFeedItems: List<RestaurantFeedItem> =
                        snapshot.children.map { Moro ->
                            Moro.getValue(RestaurantFeedItem::class.java)!!
                        }
                    Log.d("ITEMS", RestaurantFeedItems.toString())
                    Log.d("ITEMSS", moro.toString())
//                    for(h in snapshot.children){
//                        val product = h.getValue(RestaurantFeedItem::class.java)
//                        Log.d("kommentti", product.toString())
////                        productList.add(product!!)
//                }






        setUpTabs()


    }


        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        val view = binding.root

        //setContentView(R.layout.activity_comment)
        setContentView(view)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpTabs()

        var namiska = findViewById(R.id.button4) as Button
        namiska.setOnClickListener {
            kommenttiteksti = "Ihan jees, tää tuli ohjelmasta"
            arvosanatahti = 3.5
            val productId: String? = ref.push().key
            val item = kommentti(arvosanatahti, kommenttiteksti)

            ref.child(productId.toString()).setValue(item).addOnCompleteListener {
                Toast.makeText(applicationContext, "Kommentti jätetty", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.icLogOut)?.setVisible(false)
        menu?.findItem(R.id.icLogin)?.setVisible(false)
        menu?.findItem(R.id.profile)?.setVisible(false)
        return super.onPrepareOptionsMenu(menu)
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







        //  binding.moro.setText(moro)
        // Log.d("Firebase lista" , productList.size.toString())
        adapter.addFragment(BarFrag1(), markerdata.toString())
        adapter.addFragment(BarFrag2(), "Tapahtumat")
        adapter.addFragment(BarFrag3(), "Kommentit")

        binding.viewPager.adapter = adapter
        binding.bartablayout.setupWithViewPager(binding.viewPager)


    }

}