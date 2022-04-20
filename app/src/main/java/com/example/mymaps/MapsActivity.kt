package com.example.mymaps


//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.getrestaurantdata.Record
import com.example.getrestaurantdata.ValDataone
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class MapsActivity : AppCompatActivity(), OnMapReadyCallback, (MutableList<Address>) -> Unit {
  //  private lateinit var mMap: GoogleMap
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    lateinit var toggle: ActionBarDrawerToggle
        var loggedIn = false

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }
    private lateinit var mMap: GoogleMap
    var jsonresponsedata: List<Record> = mutableListOf()
    var secondPostalcode: String = ""
    lateinit var postalCode: String
   lateinit var city: String
    lateinit var totalAddress: String
    var addressList: MutableList<String> = mutableListOf()
lateinit var getlocation: Address
    private lateinit var clusterManager: ClusterManager<getdata>
    lateinit var bounds: LatLngBounds


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setSupportActionBar(findViewById(R.id.toolbar))
        toggle = ActionBarDrawerToggle(
            this,
            findViewById(R.id.drawer_layout),
            R.string.open,
            R.string.close
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        auth = Firebase.auth
        onStartUp()
        loggedIn = onStartUp()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient((this))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return true
    }

   
override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.icLogOut)?.setVisible(false)
        if (intent.hasExtra("loggedIn")) {
            loggedIn = intent.extras?.get("loggedIn") as Boolean
        }
        if (loggedIn) {
            menu?.findItem(R.id.icLogin)?.setVisible(false)

        } else {
            menu?.findItem(R.id.profile)?.setVisible(false)
        }
        return true
    }

    private fun reload() {

    }



    private fun onStartUp(): Boolean {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        val siEmail = currentUser?.email
        Toast.makeText(this, "Sisäänkirjauduttu spostilla " + siEmail.toString(), Toast.LENGTH_SHORT).show()
        if(currentUser != null){
            reload()
            return true
        }else return false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }
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
//clusterManager.setOnClusterClickListener(mOnClusterClickListener)

        mMap = googleMap

        clusterManager = ClusterManager<getdata>(this@MapsActivity, mMap)
//        val mRenderer = CustomClusterRenderer(this, mMap, clusterManager)
//        mRende
       // clusterManager.setRenderer(mRenderer)

        clusterManager.setAnimation(true)
     //   map.setOnMarkerClickListener(mClusterManager);


//        clusterManager.setOnClusterItemClickListener { getdata ->
//
//
//            val clusterRender = clusterManager.renderer as DefaultClusterRenderer
//
//
//         val moro = clusterRender.getMarker(getdata).title
//
//
//            clusterRender.getMarker(getdata).showInfoWindow()
//        Log.d("Moro, titteli", moro.toString())
//
//
//
//
//
////
////            beachMarker.isSelected = true
////            val marker = clusterRender.getMarker(beachMarker)
////            clusterRender.onClusterItemChange(beachMarker, marker)
////            lastSelectedBeachMarker = beachMarker
//            true
//        }
        clusterManager.setOnClusterItemInfoWindowClickListener() { getdata ->


            val clusterRender = clusterManager.renderer as DefaultClusterRenderer


 val info = clusterRender.getMarker(getdata).title
            Log.d("Moro, info ikkunasta", info.toString())
            val intent = Intent(this, CommentActivity::class.java)
intent.putExtra("Ravintola", info)
            intent.putExtra("IsLoggedInData", loggedIn)
            Log.d("Logdata:", loggedIn.toString())
                startActivity(intent)
            true
        }

        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.mapstyle
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
                bounds = mMap.projection.visibleRegion.latLngBounds
//                Log.d("Kamera", float.toString())
                if (float > 13) {

                    GlobalScope.launch (Dispatchers.Main) {
                        luearvot()

                    }





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








    private fun luearvot() {
//        var markerBitmap =
//            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_local_bar_24, null)
//                ?.toBitmap()
//        val icon = markerBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }
     //    val arvoja = moro()
        val geocoder = Geocoder(this@MapsActivity)
        val lon = mMap.cameraPosition.target.longitude
        val lat = mMap.cameraPosition.target.latitude
        val addresses = geocoder.getFromLocation(lat, lon, 1)


        if (addresses != null && addresses.size > 0) {
            city = addresses[0].locality

             postalCode = addresses[0]?.postalCode.toString()!!
if(postalCode == null){ postalCode == secondPostalcode}


            Log.d("Postinumero", postalCode)
            totalAddress =
                "data/fi/api/3/action/datastore_search?q=anniskelu%20a%20${postalCode}%20${city}&resource_id=2ce47026-377f-4837-b26f-610626be0ac1&limit=300"



            if (!postalCode.equals(secondPostalcode) && postalCode != null) {
               // Thread(Runnable{


              //  withContext(Dispatchers.IO) {
                    clusterManager.clearItems()

                    with(Api) {
                        retrofitService.getAllData(totalAddress)
                            .enqueue(object : Callback<ValDataone> {
                                //    @RequiresApi(33)
                                override fun onResponse(
                                    call: Call<ValDataone>,
                                    response: Response<ValDataone>
                                ) {
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


                                                val location = geocoder.getFromLocationName(
                                                    it.OSOITE, 1
                                                )
                                                getlocation = location.first()

                                            } catch (e: Exception) {
                                                //   Log.d("Virhe", "Ei toimi")
                                            }

                                            val dataitem = getdata(
                                                getlocation.latitude,
                                                getlocation.longitude,
                                                it.NIMI,
                                                it.OSOITE,
                                           //     R.drawable.ic_baseline_local_bar_24
                                            )


                                            clusterManager.addItem(dataitem)


                                        }
//                                        render = CustomClusterRenderer(this@MapsActivity, mMap, clusterManager)
//                                        clusterManager.setRenderer(render)
clusterManager.cluster()







                                    }
                                    if (jsonresponsedata.size != 0)
                                        secondPostalcode =
                                            jsonresponsedata.elementAt(0).POSTINUMERO.toString()

                                }

                                override fun onFailure(call: Call<ValDataone>, t: Throwable) {
                                    t.printStackTrace()
                                    Log.i("virhe", "virhe")
                                }
                            })


          //          }

//arvoja.viewModelScope.coroutineContext
                }
             //   }).start()


/*

Firebase branchista


 setupMap()


    }

    private fun setupMap() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                //placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))


 */


            }

        }



    }

/*
Firebasesta 

    private fun placeMarkerOnMap(currentLatLng: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("$currentLatLng")
        mMap.addMarker((markerOptions))
    }



 */

//    override fun onMarkerClick(p0: Marker) = false{
//}



//   protected fun onBeforeClusterRendered(
//        cluster: Cluster<getdata?>?,
//        markerOptions: MarkerOptions
//    ) {
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_local_bar_24))
//    }
//     suspend fun getAllData() = coroutineScope {
//         GlobalScope.launch (Dispatchers.IO) {
//             doAsync {
////        var markerBitmap =
////            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_local_bar_24, null)
////                ?.toBitmap()
////        val icon = markerBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }
//       //  val listener = GeocodeListener(this@MapsActivity)
//    val geocoder = Geocoder(this@MapsActivity)
//
//    with(Api) {
//        retrofitService.getAllData(totalAddress).enqueue(object : Callback<ValDataone> {
//        //    @RequiresApi(33)
//            override fun onResponse(call: Call<ValDataone>, response: Response<ValDataone>) {
//                if (response.isSuccessful) {
//
//                    //  val adapter = moshi.adapter<List<Record>>()
//                    // val cards: List<Record> = adapter.fromJson(Datarecord)
//
//                    // val type = Types.newParameterizedType(List::class.java, List<Record>::class.java)
//
//                    //  val adapter = moshi.adapter<List<String>>(type)
//                    //  val moshi = Moshi.Builder()
//                    // .add(KotlinJsonAdapterFactory())
//                    // .build()
//                    //val allNames: List<String>? = adapter.fromJson(response.body()?.result?.records)
//
//
//                    jsonresponsedata = response.body()?.result?.records!!
//                    jsonresponsedata.stream().forEach() {
//
//
//
//
//                        try {
//
//
//                                val location = geocoder.getFromLocationName(it.OSOITE,1
//                                )
//                            getlocation = location.first()
//
//                        } catch (e: Exception) {
//                         //   Log.d("Virhe", "Ei toimi")
//                        }
//
//                        val dataitem = getdata(getlocation.latitude,getlocation.longitude, it.NIMI, it.OSOITE, R.drawable.ic_baseline_local_bar_24)
//                        clusterManager.addItem(dataitem)
//
//
//
//                    }
//
//                  clusterManager.cluster()
//
//                }
//                if(jsonresponsedata.size != 0)
//                    secondPostalcode = jsonresponsedata.elementAt(0).POSTINUMERO.toString()
////                Log.d("jsonresponse.lastindex", jsonresponsedata.lastIndex.toString())
//
//            }
//
//            override fun onFailure(call: Call<ValDataone>, t: Throwable) {
//                t.printStackTrace()
//                Log.i("virhe", "virhe")
//            }
//        })
//
//}
//
//
//     }

//    override fun invoke(p1: MutableList<Address>) {
//
//    }






    override fun invoke(p1: MutableList<Address>) {

    }

}

//inline fun dataitem(crossinline f: () -> Unit) {
//    Thread({ f() }).start()
//}
//class SimpleThread(allData: Unit) : Thread() {
//    //val mapsi = MapsActivity()
//    public override fun run() {
//    Unit
//    }
//}
