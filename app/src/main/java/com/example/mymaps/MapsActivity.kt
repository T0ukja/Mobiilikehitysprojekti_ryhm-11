package com.example.mymaps


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.getrestaurantdata.Record
import com.example.getrestaurantdata.ValDataone
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, (MutableList<Address>) -> Unit {

    private lateinit var mMap: GoogleMap
    var jsonresponsedata: List<Record> = mutableListOf()
    var secondPostalcode: String = ""
    lateinit var postalCode: String
 //   lateinit var city: String
    lateinit var totalAddress: String
    var addressList: MutableList<String> = mutableListOf()
lateinit var getlocation: Address
    private lateinit var clusterManager: ClusterManager<getdata>
    lateinit var bounds: LatLngBounds


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


    @SuppressLint("SuspiciousIndentation")
    override fun onMapReady(googleMap: GoogleMap) {


        mMap = googleMap
        clusterManager = ClusterManager(this@MapsActivity, mMap)
        clusterManager.setAnimation(true)

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
//                Log.d("Kamera", float.toString())
                if (float > 13) {

                    bounds = mMap.projection.visibleRegion.latLngBounds

                    GlobalScope.launch (Dispatchers.Main) {  luearvot() }
if(float > 13.1){
    clusterManager.cluster()

}




                }
                    else {
                    clusterManager.clearItems()
                    clusterManager.cluster()
                    addressList.clear()
                    secondPostalcode = ""

                }
            }


    }
    }



     private suspend fun luearvot() {
        val geocoder = Geocoder(this@MapsActivity)
        val lon = mMap.cameraPosition.target.longitude
        val lat = mMap.cameraPosition.target.latitude
        val addresses = geocoder.getFromLocation(lat, lon, 1)


        if (addresses != null && addresses.size > 0) {

          //  city = addresses[0].locality

             postalCode = addresses[0]?.postalCode!!
if(postalCode == null)
    postalCode == secondPostalcode
            totalAddress =
                "data/fi/api/3/action/datastore_search?q=anniskelu%20a%20${postalCode}%20&resource_id=2ce47026-377f-4837-b26f-610626be0ac1"



            if (!postalCode.equals(secondPostalcode) && postalCode != null) {
clusterManager.clearItems()

 getAllData()

            }


        }



    }

     suspend fun getAllData() = coroutineScope {
         GlobalScope.launch (Dispatchers.IO) {
             doAsync {
//        var markerBitmap =
//            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_local_bar_24, null)
//                ?.toBitmap()
//        val icon = markerBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }
       //  val listener = GeocodeListener(this@MapsActivity)
    val geocoder = Geocoder(this@MapsActivity)

    with(Api) {
        retrofitService.getAllData(totalAddress).enqueue(object : Callback<ValDataone> {
        //    @RequiresApi(33)
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
                    jsonresponsedata.stream().forEach() {




                        try {


                                val location = geocoder.getFromLocationName(it.OSOITE,1
                                )
                            getlocation = location.first()

                        } catch (e: Exception) {
                         //   Log.d("Virhe", "Ei toimi")
                        }

                        val dataitem = getdata(getlocation.latitude,getlocation.longitude, it.NIMI, it.OSOITE)
                        clusterManager.addItem(dataitem)



                    }

                  clusterManager.cluster()

                }
                if(jsonresponsedata.size != 0)
                    secondPostalcode = jsonresponsedata.elementAt(0).POSTINUMERO.toString()
//                Log.d("jsonresponse.lastindex", jsonresponsedata.lastIndex.toString())

            }

            override fun onFailure(call: Call<ValDataone>, t: Throwable) {
                t.printStackTrace()
                Log.i("virhe", "virhe")
            }
        })

}


     }
//    override fun invoke(p1: MutableList<Address>) {
//    }
         }
     }

    override fun invoke(p1: MutableList<Address>) {

    }
}

//inline fun dataitem(crossinline f: () -> Unit) {
//    Thread({ f() }).start()
//}
