package com.example.mymaps

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class getdata(latitude: Double, longitude: Double, nimi: String, rank: String, pictureResource: Int) : ClusterItem {

    private val profilePhoto: Int
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
    init {
        position = LatLng(latitude, longitude)
        this.title = nimi
        this.snippet = rank
        this.profilePhoto = pictureResource
    }


}