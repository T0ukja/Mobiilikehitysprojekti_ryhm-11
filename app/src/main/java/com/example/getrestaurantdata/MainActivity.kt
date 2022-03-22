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

class MainActivity : AppCompatActivity() {

    lateinit var data: ArrayList<ValDataone>
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getAllData()

    }


    fun getAllData(){
        with(Api) {
            retrofitService.getAllData().enqueue(object: Callback<ValDataone> {
                override fun onResponse(call: Call<ValDataone>, response: Response<ValDataone>) {

                    if(response.isSuccessful){
                        val ArrayList = response.body()?.result?.records
                        Log.d("KUNTAAA :", "MORO")
                        println(response.body())
                      //  val stringResponse = response.body()?.toString()
                       val listofmodels = response.body()
                      //data = response.body() as ArrayList<ValDataone>
                        //  var json = JSONObject(listofmodels?.result?.toString()) // toString() is not the response body, it is a debug representation of the response body
  ///

                        val koko = ArrayList?.size
                    //
                    // var status = json.getString("KUNTA")

//                    val jsonArray = JSONArray(response.body())
                       // val jsonObject: JSONObject = jsonArray.getJSONObject(0)

                        response.body()?.result?.records?.get(6)
                            ?.let { Log.d("Pretty Printed json :", it.KUNTA)


                            }

                        Log.d("json :", ArrayList?.get(ArrayList.size-1).toString())
                        Log.i("jjsona", koko.toString())
                        Log.d("jjson", ArrayList?.get(ArrayList.size-2).toString())
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