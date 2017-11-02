package com.brandonlamb.cars.domain

data class Car(val make: CarMake, val model: String, val color: String)
data class Cars(val cars: List<Car>, val total: Int)
data class CarFilter(val make: String?, val limit: Int, val offset: Int)

enum class CarMake {
  FORD, TOYOTA, CHEVROLET
}