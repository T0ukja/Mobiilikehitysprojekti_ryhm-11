package com.example.mymaps

import com.example.getrestaurantdata.ValDataone
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://www.avoindata.fi/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL).build()



interface ApiService {


    @GET("{address}")
    fun getAllData(@Path("address") address: String): Call<ValDataone>


}


object Api{

    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}