package com.example.mymaps


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.getrestaurantdata.Record
import com.example.getrestaurantdata.ValDataone
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.navigation.NavigationView
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




class MapsActivity : AppCompatActivity(), OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener, (MutableList<Address>) -> Unit {
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient
  //  private lateinit var lastLocation: Location
   lateinit var toggle: ActionBarDrawerToggle
        var loggedIn = false

//    companion object {
//        private const val LOCATION_REQUEST_CODE = 1
//    }


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
        setNavigationViewListener()

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

    private fun setNavigationViewListener() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val eventFrag: Fragment = EventFragment()
        val bestFrag: Fragment = BestFragment()
        val offerFrag: Fragment = OfferFragment()
        when (item.itemId) {
            R.id.events -> {
                supportFragmentManager.popBackStackImmediate()
                supportFragmentManager.beginTransaction().replace(R.id.map, eventFrag)
                    .addToBackStack("events")
                    .commit()
                println(supportFragmentManager.fragments)
                return true
            }
            R.id.home -> {
                supportFragmentManager.popBackStackImmediate()
                return true
            }
            R.id.offers -> {
                supportFragmentManager.popBackStackImmediate()
                supportFragmentManager.beginTransaction().replace(R.id.map, offerFrag)
                    .addToBackStack("offers").commit()
                return true
            }
            R.id.best -> {
                supportFragmentManager.popBackStackImmediate()
                supportFragmentManager.beginTransaction().replace(R.id.map, bestFrag)
                    .addToBackStack("best").commit()
                return true
            }
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
        if (currentUser != null) {
            Toast.makeText(
                this,
                "Sisäänkirjauduttu spostilla " + siEmail.toString(),
                Toast.LENGTH_SHORT
            ).show()
            reload()
            return true
        } else return false
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


        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        clusterManager = ClusterManager<getdata>(this@MapsActivity, mMap)

        clusterManager.setAnimation(true)


        clusterManager.setOnClusterItemInfoWindowClickListener() { getdata ->


            val clusterRender = clusterManager.renderer as DefaultClusterRenderer


 val info = clusterRender.getMarker(getdata).title
            val intent = Intent(this, CommentActivity::class.java)
intent.putExtra("Ravintola", info)
            intent.putExtra("IsLoggedInData", loggedIn)
                startActivity(intent)


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
                if (float > 13) {

                    GlobalScope.launch(Dispatchers.Main) {
                        luearvot()

                    }





                    if (float > 13.1) {
                        clusterManager.cluster()

                    }


                } else {
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


                    with(Api) {
                        retrofitService.getAllData(totalAddress)
                            .enqueue(object : Callback<ValDataone> {

                                override fun onResponse(
                                    call: Call<ValDataone>,
                                    response: Response<ValDataone>
                                ) {
                                    if (response.isSuccessful) {

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

                                        clusterManager.cluster()


                                }
                                if (jsonresponsedata.size != 0)
                                    secondPostalcode =
                                        jsonresponsedata.elementAt(0).POSTINUMERO.toString()

                            }

                                override fun onFailure(call: Call<ValDataone>, t: Throwable) {
                                    Log.i("virhe", "virhe")
                                }


                            })
                    }



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

    override fun invoke(p1: MutableList<Address>) {

    }

}


//    override fun invoke(p1: MutableList<Address>) {
//
//    }



