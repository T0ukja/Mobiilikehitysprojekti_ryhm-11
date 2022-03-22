package com.example.mymaps

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.getrestaurantdata.ValDataone
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

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
        return when(item.itemId) {
            R.id.icLogin -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return true
            }else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        getAllData()


        val markerBitmap =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_local_bar_24, null)
                ?.toBitmap()
        val icon = markerBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) }

        // Add a marker in Sydney and move the camera
        val oulu = LatLng(65.012360, 25.468160)
        mMap!!.addMarker(MarkerOptions().position(oulu).title("Bar in Oulu").icon(icon))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(oulu))

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

    }



    fun getAllData(){
        val firstRecord = "data/fi/api/3/action/datastore_search?q=anniskelu%20a&resource_id=2ce47026-377f-4837-b26f-610626be0ac1&limit=7991"

        with(Api) {
            retrofitService.getAllData(firstRecord).enqueue(object: Callback<ValDataone> {
                override fun onResponse(call: Call<ValDataone>, response: Response<ValDataone>) {

                    if(response.isSuccessful){
                        var Datarecord = response.body()?.result?.records
                        val Maxdata = response.body()?.result?.total?.toInt()
                        var Nextlink = response.body()?.result?._links?.next
                        val koko = Datarecord?.size



                        val listofmodels = response.body()
                        //data = response.body() as ArrayList<ValDataone>
                        //  var json = JSONObject(listofmodels?.result?.toString()) // toString() is not the response body, it is a debug representation of the response body
                        //
                        //
                        // var status = json.getString("KUNTA")

//                    val jsonArray = JSONArray(response.body())
                        // val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                        for (i in 0 until Maxdata!! -1) {
                            response.body()?.result?.records?.get(i)
                                ?.let {

                                    Log.d("JSON Nimi :", it.NIMI)
                                    Log.d("JSON ID :", it._id.toString())
                                    Log.d("OSOITE JSON", it.OSOITE)
                                    Log.d("JSON Kunta :", it.KUNTA)
                                }

                        }
                        Log.i("Paljonko on haettu, JSON", koko.toString())
                        Log.i("Seuraava linkki JSON", Nextlink.toString())
                        Log.i("Montako ravintolaa, JSON", Maxdata.toString())


                        // Log.d("json :", Datarecord?.get(Datarecord.size-1).toString())
                        // Log.d("jjson", Datarecord?.get(Datarecord.size-2).toString())
                        //Log.d("KUNTAAA :", jsonObject.getString("KUNTA"))
                        // println(response.body())

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
