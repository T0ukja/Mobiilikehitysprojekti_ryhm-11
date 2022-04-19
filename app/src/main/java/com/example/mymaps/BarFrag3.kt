package com.example.mymaps

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymaps.adapters.Myadapter
import com.example.mymaps.databinding.ActivityCommentBinding
import com.google.firebase.database.*


class BarFrag3(markerdata: String?) : Fragment() {

    var tieotja = markerdata
private lateinit var userRecyclerView: RecyclerView
        private lateinit var userArrayList: ArrayList<kommentti>
//lateinit var Moro : List<kommentti>
    data class RestaurantFeedItem(
        val title: String = "",
        val description: String =""
    )


    data class kommentti(
        val arvosana: Double = 0.0,
        val palaute: String =""
    )
    override fun onCreate(savedInstanceState: Bundle?) {

userArrayList = arrayListOf<kommentti>()

        super.onCreate(savedInstanceState)
        var binding = ActivityCommentBinding.inflate(layoutInflater)
//    val bundle = intent.extras
//        val myString = args!!.getString("key").toString()
//        Log.d("Moro mystring", myString.toString())




//        var namiska = findViewById(R.id.givecommentbtn) as Button
//
//   Button myButton = (Button) root.findViewById(R.id.givecommentbtn);
//        super.onCreate(savedInstanceState)
        arguments?.let {

        }





    }
lateinit var mview : View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        val mview: View = inflater.inflate(R.layout.fragment_bar_frag3, container, false)

        val fab = mview.findViewById<Button>(R.id.fab)

        fab.setOnClickListener { view ->

            showDialog()
        }

/*val moro = mview.findViewById<Button>(R.id.fab)
        moro.setOnClickListener{
            Log.d("MOro2", "Moro fragmentista")

//            Log.d("User arraylist", userArrayList.toString())

        }*/
        userRecyclerView = mview.findViewById(R.id.kommenttiList)
        userRecyclerView.layoutManager = LinearLayoutManager(mview.context)
        userRecyclerView.setHasFixedSize(true)
// = arrayListOf<kommentti>()
   getuserdata()
        // Inflate the layout for this fragment
        return mview

    }
   private fun getuserdata(){

      // var markerdata: String = ""
       lateinit var ref: DatabaseReference
       //  lateinit var listView: ListView
       lateinit var kommenttiteksti: String
       var arvosanatahti: Double = 0.0


//       val args = arguments
//val mmm = args!!.getString("key")!!
//       Log.d("Moro markerdata", mmm)

//           markerdata = getActivity()?.getIntent()?.getExtras()?.getString("ravintolat").toString();
//             markerdata = intent.getStringExtra("Ravintola")!!
           ref = FirebaseDatabase.getInstance().getReference("Ravintolat").child(tieotja.toString()).child("Kommentit")
           //productList = mutableListOf()

           ref.addValueEventListener(object: ValueEventListener {



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

               }

               // *YLLÄ* tietokantaan viittaavaan ref-muuttujaan asetetaan tietynlainen tiedonkuuntelija
               // jonka avulla tietokannasta haetaan snapshot *ALLA* eli hakutulos
               // olemassaolevista kentistä ja asetetaan tiedon näkymän toteuttavan ja
               // oikeassa muodossa tulostavan adapterin kautta sille tarkoitetulle listalle.

                override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot!!.exists()){
                       //       productList.clear()
                       Log.d("SNAPSHOT", snapshot.value.toString())
                 //      Log.d("SssssNAPSHOT", markerdata)
                 //      Log.d("markerdata", markerdata)


                       for (userSnapshots in snapshot.children){
                           val moro = userSnapshots.getValue(kommentti::class.java)
                           Log.d("Moro loop", moro.toString())
                           userArrayList.add(moro!!)
                       }

//
//                       Moro = snapshot.children.map { Moro ->
//                           Moro.getValue(kommentti::class.java)!!
//
//                       }


                       userRecyclerView.adapter= Myadapter(userArrayList)
                   //    userArrayList = moro as List<kommentti>

//                    val RestaurantFeedItems: List<RestaurantFeedItem> = snapshot.children.map { Moro ->
//                        Moro.getValue(RestaurantFeedItem::class.java)!!
//                    }
                       //  Log.d("ITEMS", RestaurantFeedItems.toString())
                 //      Log.d("ITEMSS", Moro.toString())
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

    private fun showDialog() {



        val builder = AlertDialog.Builder(this.context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.kommentti_dialog, null)
        var palauteET = dialogLayout.findViewById<EditText>(R.id.palauteET)
        var kommenttiET = dialogLayout.findViewById<EditText>(R.id.kommenttiET)

        with(builder) {
            setTitle("Lisää arvosana ja kommentti")
            setPositiveButton("lisää"){dialog, which ->

            }
            setNegativeButton("Palaa"){dialog, which ->

            }
            setView(dialogLayout)
            show()
        }


    }




}