package com.example.getrestaurantdata

import com.squareup.moshi.Json

data class Record(
    val KUNTA: String,
    val LUPATYYPPI: String,
    val LUVANALKUPVM: String,
    val NIMI: String,
    val OSOITE: String,
    val POSTINUMERO: Int,
    val POSTITOIMIPAIKKA: String,
    @Json(name="Y-TUNNUS")
    val Y_TUNNUS: String,
    val _id: Int,
    val rank: Double
)