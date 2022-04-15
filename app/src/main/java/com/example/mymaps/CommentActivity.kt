package com.example.mymaps

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mymaps.adapters.ViewPagerAdapter
import com.example.mymaps.databinding.ActivityCommentBinding
import com.google.firebase.database.*


class CommentActivity : AppCompatActivity() {
   // lateinit var productList: MutableList<Reviews>

    private lateinit var binding: ActivityCommentBinding
    lateinit var ref: DatabaseReference
  //  lateinit var listView: ListView


    data class RestaurantFeedItem(
        val title: String = "",
        val description: String =""
    )


    data class kommentti(
        val Arvosanna: Double = 0.0,
        val Palaute: String =""
    )
    init{
    ref = FirebaseDatabase.getInstance().getReference("Ravintolat")
    //productList = mutableListOf()

    ref.addValueEventListener(object: ValueEventListener {

        override fun onCancelled(error: DatabaseError) {

        }

        // *YLLÄ* tietokantaan viittaavaan ref-muuttujaan asetetaan tietynlainen tiedonkuuntelija
        // jonka avulla tietokannasta haetaan snapshot *ALLA* eli hakutulos
        // olemassaolevista kentistä ja asetetaan tiedon näkymän toteuttavan ja
        // oikeassa muodossa tulostavan adapterin kautta sille tarkoitetulle listalle.

        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot!!.exists()){
                //       productList.clear()
                Log.d("SNAPSHOT", snapshot.value.toString())


                val moro: List<kommentti> = snapshot.children.map { Moro ->
                    Moro.getValue(kommentti::class.java)!!
                }

                val RestaurantFeedItems: List<RestaurantFeedItem> = snapshot.children.map { Moro ->
                    Moro.getValue(RestaurantFeedItem::class.java)!!
                }
             Log.d("ITEMS", RestaurantFeedItems.toString())
                Log.d("ITEMSS", moro.toString())
//                    for(h in snapshot.children){
//                        val product = h.getValue(RestaurantFeedItem::class.java)
//                        Log.d("kommentti", product.toString())
////                        productList.add(product!!)
//                }

//                    for(h in snapshot.children){
//                        val product = h.getValue(Reviews::class.java)
//                        productList.add(product!!)
//                    }
//                    val adapter = ProductAdapter(this@CommentActivity, R.layout.single_item, productList)
//                    //listView.adapter = adapter
            }
        }

    });
}
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
       // Log.d("Firebase lista" , productList.size.toString())
        adapter.addFragment(BarFrag1(), markerdata.toString())
        adapter.addFragment(BarFrag2(), "Tapahtumat")
        adapter.addFragment(BarFrag3(), "Kommentit")
        binding.viewPager.adapter = adapter
        binding.bartablayout.setupWithViewPager(binding.viewPager)


    }

}