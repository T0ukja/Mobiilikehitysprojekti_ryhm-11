package com.example.getrestaurantdata

data class Result(
    val _links: Links,
    val fields: List<Field>,
    val include_total: Boolean,
    val q: String,
    val records: List<Record>,
    val records_format: String,
    val resource_id: String,
    val total: Int
)