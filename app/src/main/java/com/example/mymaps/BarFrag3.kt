package com.example.mymaps

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymaps.adapters.Myadapter
import com.example.mymaps.databinding.ActivityCommentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*


class BarFrag3(markerdata: String?, loggedIn: Boolean) : Fragment() {

    var restaurantname = markerdata
    var IsLoggedIn = loggedIn
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<kommentti>

    data class kommentti(
        val arvosana: Double = 0.0,
        val palaute: String = ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        userArrayList = arrayListOf<kommentti>()

        super.onCreate(savedInstanceState)
        var binding = ActivityCommentBinding.inflate(layoutInflater)

        arguments?.let {

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        val mview: View = inflater.inflate(R.layout.fragment_bar_frag3, container, false)

        val fab = mview.findViewById<FloatingActionButton>(R.id.fab)

        if (IsLoggedIn) {
          //  mview.findViewById<Button>(R.id.fab).visibility = View.VISIBLE
fab.show()
        } else {
           // mview.findViewById<Button>(R.id.fab).visibility = View.GONE
            fab.hide()
        }

        fab.setOnClickListener { view ->

            showDialog()
        }


        userRecyclerView = mview.findViewById(R.id.kommenttiList)
        userRecyclerView.layoutManager = LinearLayoutManager(mview.context)
        userRecyclerView.setHasFixedSize(true)

        getuserdata()
        // Inflate the layout for this fragment
        return mview

    }

    private fun getuserdata() {


        lateinit var ref: DatabaseReference

        ref = FirebaseDatabase.getInstance().getReference("Ravintolat").child(restaurantname.toString())
            .child("Kommentit")


        ref.addValueEventListener(object : ValueEventListener {


            override fun onCancelled(error: DatabaseError) {

            }


            override fun onDataChange(snapshot: DataSnapshot) {

                userArrayList.clear()

                if (snapshot!!.exists()) {


                    for (userSnapshots in snapshot.children) {
                        val userComment = userSnapshots.getValue(kommentti::class.java)
                        userArrayList.add(userComment!!)
                    }

                    userRecyclerView.adapter = Myadapter(userArrayList)

                }
            }

        });


    }

    private fun showDialog() {

        var RBrating = 0.0
        val builder = AlertDialog.Builder(this.context)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.kommentti_dialog, null)
        val palauteRB = dialogLayout.findViewById<RatingBar>(R.id.palauteRB)
        val kommenttiET = dialogLayout.findViewById<EditText>(R.id.kommenttiET)

        palauteRB.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            RBrating = rating.toDouble()
        }


        with(builder) {
            setTitle("Lisää arvosana ja kommentti")
            lateinit var ref: DatabaseReference
            ref =
                FirebaseDatabase.getInstance().getReference("Ravintolat").child(restaurantname.toString())
                    .child("Kommentit")
            setPositiveButton("lisää") { dialog, which ->

                val productId: String? = ref.push().key
                val item = CommentActivity.kommentti(RBrating, kommenttiET.text.toString())

                ref.child(productId.toString()).setValue(item).addOnCompleteListener {
                }
            }
            setNegativeButton("Palaa") { dialog, which ->


            }

            setView(dialogLayout)

            show()


        }


    }


}