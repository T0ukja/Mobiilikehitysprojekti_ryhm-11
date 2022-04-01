package com.example.getrestaurantdata

import com.squareup.moshi.Json

data class Result(
    val _links: Links,
    val fields: List<Field>,
    val include_total: Boolean,
    val q: String,
//    @Json(name = "Recordlisting")
    val records: List<Record>,
    val records_format: String,
    val resource_id: String,
    val total: Int
)