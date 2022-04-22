package com.example.mymaps.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymaps.BarFrag3
import com.example.mymaps.R

class Myadapter(private val userlist: ArrayList<BarFrag3.kommentti>) :
    RecyclerView.Adapter<Myadapter.MyViewHolder>() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myadapter.MyViewHolder {
 val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_comment, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Myadapter.MyViewHolder, position: Int) {
       val currentitem = userlist[position]

        holder.comments.text= currentitem.palaute
        holder.starrating.rating= currentitem.arvosana.toFloat()
    }

    override fun getItemCount(): Int {
return userlist.size
    }



    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
val comments : TextView = itemView.findViewById(R.id.tvKommentti)
        val starrating : RatingBar = itemView.findViewById(R.id.tvPalaute)
    }
}