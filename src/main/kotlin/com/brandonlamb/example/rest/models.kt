package com.brandonlamb.example.rest

import javax.json.bind.annotation.JsonbProperty

data class Car(@get:JsonbProperty("customMake") val make: String, val model: String, val color: String)
data class Cars(val cars: List<Car>, val paging: Paging)
data class Paging(val total: Int, val limit: Int, val offset: Int)
