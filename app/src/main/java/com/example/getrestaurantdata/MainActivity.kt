package com.example.getrestaurantdata

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import org.json.JSONArray
import org.json.JSONObject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
val firstRecord = "data/fi/api/3/action/datastore_search?q=anniskelu%20a&resource_id=2ce47026-377f-4837-b26f-610626be0ac1&limit=7991"
class MainActivity : AppCompatActivity() {

    lateinit var data: ArrayList<ValDataone>
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getAllData()

    }


    fun getAllData(){
        with(Api) {
            retrofitService.getAllData(firstRecord).enqueue(object: Callback<ValDataone> {
                override fun onResponse(call: Call<ValDataone>, response: Response<ValDataone>) {

                    if(response.isSuccessful){
                        var Datarecord = response.body()?.result?.records
                        val Maxdata = response.body()?.result?.total
                        var Nextlink = response.body()?.result?._links?.next
                        val koko = Datarecord?.size

                        Log.d("KUNTAAA :", "MORO")
                        println(response.body())
                      //  val stringResponse = response.body()?.toString()
                       val listofmodels = response.body()
                      //data = response.body() as ArrayList<ValDataone>
                        //  var json = JSONObject(listofmodels?.result?.toString()) // toString() is not the response body, it is a debug representation of the response body
  ///


                    //
                    // var status = json.getString("KUNTA")

//                    val jsonArray = JSONArray(response.body())
                       // val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                        for (i in 1..Maxdata!!) {
                            response.body()?.result?.records?.get(i)
                                ?.let {
                                    Log.d("JSON Kunta :", it.KUNTA)
                                    Log.d("JSON Nimi :", it.NIMI)
                                    Log.d("JSON ID :", it._id.toString())
                                    Log.d("OSOITE JSON", it.OSOITE)

                                }

                        }
                        response.body()?.result?.records?.get(6)
                            ?.let { Log.d("JSON Kunta :", it.KUNTA)


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