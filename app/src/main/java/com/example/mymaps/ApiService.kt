package com.example.mymaps

import android.location.Address
import com.example.getrestaurantdata.ValDataone
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://www.avoindata.fi/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL).build()

interface ApiService {

    @GET("data/fi/api/3/action/datastore_search?q=anniskelu%20a&resource_id=2ce47026-377f-4837-b26f-610626be0ac1&limit=7991")
    fun getAllData(): Call<ValDataone>


}


object Api{
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}