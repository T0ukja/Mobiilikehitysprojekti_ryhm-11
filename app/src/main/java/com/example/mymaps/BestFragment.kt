package com.example.mymaps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.MarginLayoutParamsCompat
import com.google.firebase.database.DatabaseReference

class BestFragment : Fragment() {

    lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_best, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layout: LinearLayout = view.findViewById(R.id.main)
        val text = TextView(context)
        val text2 = TextView(context)
        text.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        text.text = "Suosittu1"

        text2.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        text2.text = "Suosittu2"


        var lista = listOf(text, text2)
        for (item: TextView in lista) {
            layout.addView(item)
        }
/*        layout.addView(text)
        layout.addView(text2)*/
        super.onViewCreated(view, savedInstanceState)
    }
}