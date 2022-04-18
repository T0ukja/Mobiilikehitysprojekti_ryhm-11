package com.example.mymaps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mymaps.databinding.ActivityCommentBinding


class BarFrag3 : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityCommentBinding.inflate(layoutInflater)


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
val moro = mview.findViewById<Button>(R.id.givecommentbtn)
        moro.setOnClickListener{
            Log.d("MOro2", "Moro fragmentista")
        }

        // Inflate the layout for this fragment
        return mview
    }






}