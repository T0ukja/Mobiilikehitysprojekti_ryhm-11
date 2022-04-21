package com.example.mymaps.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymaps.BarFrag2
import com.example.mymaps.R

class Myadapterevents(private val userlist: ArrayList<BarFrag2.tapahtuma>) :
    RecyclerView.Adapter<Myadapterevents.MyViewHolder>() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myadapterevents.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_events, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Myadapterevents.MyViewHolder, position: Int) {
        val currentitem = userlist[position]

        holder.comments.text= currentitem.päivämäärä
        holder.starrating.text= currentitem.tapahtuma
    }

    override fun getItemCount(): Int {
        return userlist.size
    }



    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val comments : TextView = itemView.findViewById(R.id.tvKommentti)
        val starrating : TextView = itemView.findViewById(R.id.tvPalaute)
    }
}