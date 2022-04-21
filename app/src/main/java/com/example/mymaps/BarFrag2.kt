package com.example.mymaps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymaps.adapters.Myadapterevents
import com.google.firebase.database.*


class BarFrag2(markerdata: String?) : Fragment() {
    var restaurantname = markerdata

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<tapahtuma>
    data class tapahtuma(
        val päivämäärä: String = "",
        val tapahtuma: String = ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        userArrayList = arrayListOf<tapahtuma>()

        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
   // lateinit var mview: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mview: View = inflater.inflate(R.layout.fragment_bar_frag2, container, false)
       userRecyclerView = mview.findViewById(R.id.tapahtumalist)
       userRecyclerView.layoutManager = LinearLayoutManager(mview.context)
       userRecyclerView.setHasFixedSize(true)

       getuserdata()
       return mview
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_frag2, container, false)
    }
    private fun getuserdata() {


        lateinit var ref: DatabaseReference

        ref = FirebaseDatabase.getInstance().getReference("Ravintolat").child(restaurantname.toString())
            .child("Tapahtumat")


        ref.addValueEventListener(object : ValueEventListener {


            override fun onCancelled(error: DatabaseError) {

            }


            override fun onDataChange(snapshot: DataSnapshot) {

                userArrayList.clear()

                if (snapshot!!.exists()) {


                    for (userSnapshots in snapshot.children) {
                        val userComment = userSnapshots.getValue(tapahtuma::class.java)
                        userArrayList.add(userComment!!)
                    }

                    userRecyclerView.adapter = Myadapterevents(userArrayList)

                }
            }

        });


    }

}