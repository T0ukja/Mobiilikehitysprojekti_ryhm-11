package com.example.mymaps

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
import com.example.getrestaurantdata.Record
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
    var jsonresponsedata: List<Record> = mutableListOf()
    var elementData: LatLng? = null
    var secondPostalcode: String = ""
    var addressList: MutableList<String> = mutableListOf()

    lateinit var bounds: LatLngBounds
//    fun GoogleMap.isMarkerVisible(markerPosition: LatLng) =
//        projection.visibleRegion.latLngBounds.contains(markerPosition)



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
            mMap.setOnCameraIdleListener {
                // Get the current bounds of the map's visible region.
                val float = mMap.getCameraPosition().zoom
                Log.d("Testi", "Kamera liikkuu")
                Log.d("Kamera", float.toString())
                if (float > 11) {

                    bounds = mMap.projection.visibleRegion.latLngBounds

                    luearvot(bounds)


                } else {
                    mMap.clear()
                    addressList.clear()
                    secondPostalcode = ""

                }
            }

        }
    }


    fun luearvot(bounds: LatLngBounds) {
        val geocoder = Geocoder(this@MapsActivity)
        val lon = mMap.cameraPosition.target.longitude
        val lat = mMap.cameraPosition.target.latitude
        val addresses = geocoder.getFromLocation(lat, lon, 1)


        if (addresses != null && addresses.size > 0) {
//            val address =
//                addresses[0].getAddressLine(0)
      //      val city = addresses[0].locality
            val postalCode = addresses[0]?.postalCode
            val totalAddress =
                "data/fi/api/3/action/datastore_search?q=anniskelu%20a%20${postalCode}%20&resource_id=2ce47026-377f-4837-b26f-610626be0ac1"



            if (!postalCode.equals(secondPostalcode)) {
                getAllData(totalAddress)
            }


        }


    }

    fun getAllData(postalCode: String) {
        val markerBitmap =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_local_bar_24, null)
                ?.toBitmap()
        val icon = markerBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }

        val geocoder = Geocoder(this@MapsActivity)

        with(Api) {
            retrofitService.getAllData(postalCode).enqueue(object : Callback<ValDataone> {
                override fun onResponse(call: Call<ValDataone>, response: Response<ValDataone>) {
                    if (response.isSuccessful) {


                        //  val adapter = moshi.adapter<List<Record>>()
                        // val cards: List<Record> = adapter.fromJson(Datarecord)

                        // val type = Types.newParameterizedType(List::class.java, List<Record>::class.java)

                        //  val adapter = moshi.adapter<List<String>>(type)
                        //  val moshi = Moshi.Builder()
                        // .add(KotlinJsonAdapterFactory())
                        // .build()
                        //val allNames: List<String>? = adapter.fromJson(response.body()?.result?.records)

                        jsonresponsedata = response.body()?.result?.records!!
                        val koko = jsonresponsedata.size

                        jsonresponsedata.forEach {

                            try {
                                val address = geocoder.getFromLocationName(it.OSOITE, 5)
                                val location = address.first()
                                elementData = LatLng(location.latitude, location.longitude)
                            } catch (e: Exception) {
                                Log.d("Virhe", "Ei toimi")
                            }






                            if (bounds.contains(elementData!!)) {


                                if (!addressList.contains(elementData.toString())) {

                                    Log.d("Ravintolan osoite mistä tehdään markeri", it.NIMI)
//                                var markeri = MarkerOptions().position(
//                                    arvo!!)
//                                    .title(it.NIMI).snippet("Ravintolan id " +it._id.toString()).icon(icon)
                                    mMap.addMarker(MarkerOptions().position(
                                        elementData!!)
                                        .title(it.NIMI)
                                        .snippet("Ravintolan id " + it._id.toString()).icon(icon))
                                    addressList.add(elementData.toString())

                                }


                            }

                        }




                        secondPostalcode = jsonresponsedata.elementAt(0).POSTINUMERO.toString()
                        Log.d("jsonresponse.lastindex", jsonresponsedata.lastIndex.toString())
                        Log.i("Paljonko on haettu, JSON", (koko - 1).toString())
                    }


                }

                override fun onFailure(call: Call<ValDataone>, t: Throwable) {
                    t.printStackTrace()
                    Log.i("virhe", "virhe")
                }
            })
        }


    }

}


