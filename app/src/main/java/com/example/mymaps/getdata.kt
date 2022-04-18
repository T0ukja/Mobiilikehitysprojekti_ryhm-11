package com.example.mymaps

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager

class getdata(latitude: Double, longitude: Double, nimi: String, rank: String) : ClusterItem,
    ClusterManager.OnClusterInfoWindowClickListener<getdata> {

  //  private val profilePhoto: Int
    private val position: LatLng
    private val title: String
    private val snippet: String

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
return title
    }

    override fun getSnippet(): String? {
return snippet
    }

//    fun getprofilePhoto(): Int? {
//        return profilePhoto
//    }

    init {
        position = LatLng(latitude, longitude)
        this.title = nimi
        this.snippet = rank
       // this.profilePhoto = pictureResource
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<getdata>?) {
      Log.d("Moro ikkunasta", "MORO")
    }


}