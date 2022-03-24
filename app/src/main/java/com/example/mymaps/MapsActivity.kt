package com.example.mymaps

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.getrestaurantdata.ValDataone
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    fun GoogleMap.isMarkerVisible(markerPosition: LatLng) =
        projection.visibleRegion.latLngBounds.contains(markerPosition)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setSupportActionBar(findViewById(R.id.toolbar))
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //adds items to the action bar
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.icLogin -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        val bounds: LatLngBounds =
            mMap.getProjection().getVisibleRegion().latLngBounds

        getAllData(bounds)


        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.joku
                )
            )

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MapsActivity", "Can't find style. Error: ", e)
        }
        if (mMap != null) {
            // Respond to camera movements.
            mMap.setOnCameraMoveListener(GoogleMap.OnCameraMoveListener {
                // Get the current bounds of the map's visible region.
                var float = mMap.getCameraPosition().zoom
                Log.d("Testi","Kamera liikkuu")
                Log.d("Kamera", float.toString())
                if(float > 7.5) {


                    val bounds: LatLngBounds =
                        mMap.getProjection().getVisibleRegion().latLngBounds
                    getAllData(bounds)
                }

                else{
                    mMap.clear()
                }
            })

        }
    }

    fun getLocationByAddress(context: Context, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        try {
            val address = coder.getFromLocationName(strAddress, 5) ?: return null
            val location = address.first()
            return LatLng(location.latitude, location.longitude)
        } catch (e: Exception) {

        }
        return null
    }





    fun getAllData(bounds: LatLngBounds) {

        val markerBitmap =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_local_bar_24, null)
                ?.toBitmap()
        val icon = markerBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }
        with(Api) {
            retrofitService.getAllData().enqueue(object : Callback<ValDataone> {
                override fun onResponse(call: Call<ValDataone>, response: Response<ValDataone>) {

                    if (response.isSuccessful) {
                        var Datarecord = response.body()?.result?.records

                        val koko = Datarecord?.size


                        val listofmodels = response.body()


                        for (i in 0 until 5) {
                            response.body()?.result?.records?.get(i)
                                ?.let {

                                    var osoite = getLocationByAddress(this@MapsActivity, it.OSOITE)!!


                                    if (bounds.contains(osoite!!)) {
                                        // Add the marker.
                                        mMap!!.addMarker(MarkerOptions().position(
                                            osoite)
                                            .title(it.NIMI).icon(icon))

                                    }
else{
    mMap.clear()
                                    }

                                }



                        }
                        Log.i("Paljonko on haettu, JSON", koko.toString())

                    }


                }

                override fun onFailure(call: Call<ValDataone>, t: Throwable) {
                    t.printStackTrace()
                    Log.i("virhe", "virhe");
                }
            })
        }


    }


}
