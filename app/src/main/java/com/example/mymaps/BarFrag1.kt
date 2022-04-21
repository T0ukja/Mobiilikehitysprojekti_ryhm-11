package com.example.mymaps



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymaps.adapters.Myadapteroffers
import com.google.firebase.database.*


class BarFrag1(markerdata: String?) : Fragment() {
    var restaurantname = markerdata

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<tarjoukset>
    data class tarjoukset(
        val tarjous: String = "",
        val voimassa: String = ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        userArrayList = arrayListOf<tarjoukset>()

        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    // lateinit var mview: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mview: View = inflater.inflate(R.layout.fragment_bar_frag1, container, false)
        userRecyclerView = mview.findViewById(R.id.tapahtumalist)
        userRecyclerView.layoutManager = LinearLayoutManager(mview.context)
        userRecyclerView.setHasFixedSize(true)

        getuserdata()
        return mview
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_frag1, container, false)
    }
    private fun getuserdata() {


        lateinit var ref: DatabaseReference

        ref = FirebaseDatabase.getInstance().getReference("Ravintolat").child(restaurantname.toString())
            .child("Tarjoukset")


        ref.addValueEventListener(object : ValueEventListener {


            override fun onCancelled(error: DatabaseError) {

            }


            override fun onDataChange(snapshot: DataSnapshot) {

                userArrayList.clear()
                if (snapshot!!.exists()) {

                    for (userSnapshots in snapshot.children) {
                        val userComment = userSnapshots.getValue(tarjoukset::class.java)
                        userArrayList.add(userComment!!)
                    }

                    userRecyclerView.adapter = Myadapteroffers(userArrayList)
                }
               else {

Log.d("Ei löydy tarjouksia", "moro")

                }
            }

        });


    }

}