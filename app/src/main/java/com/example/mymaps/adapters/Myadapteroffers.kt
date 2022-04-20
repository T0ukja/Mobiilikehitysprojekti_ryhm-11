package com.example.mymaps.adapters
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymaps.BarFrag1
import com.example.mymaps.R

class Myadapteroffers(private val userlist: ArrayList<BarFrag1.tarjoukset>) :
    RecyclerView.Adapter<Myadapteroffers.MyViewHolder>() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myadapteroffers.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_offers, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Myadapteroffers.MyViewHolder, position: Int) {
        val currentitem = userlist[position]

        holder.comments.text= currentitem.voimassa
        holder.starrating.text= currentitem.tarjous
    }

    override fun getItemCount(): Int {
        return userlist.size
    }



    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val comments : TextView = itemView.findViewById(R.id.tvKommentti)
        val starrating : TextView = itemView.findViewById(R.id.tvPalaute)
    }
}