package com.brandonlamb.example.service

import java.util.concurrent.CompletableFuture
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
open class CarService {
  private val cars = arrayOf(
    Car(CarMake.FORD, "Pinto", "Red"),
    Car(CarMake.TOYOTA, "Camry", "Blue"),
    Car(CarMake.TOYOTA, "Camry", "Blue"),
    Car(CarMake.TOYOTA, "Camry", "Blue"),
    Car(CarMake.TOYOTA, "Camry", "Blue"),
    Car(CarMake.FORD, "Pinto", "Red"),
    Car(CarMake.CHEVROLET, "Camaro", "Blue"),
    Car(CarMake.FORD, "Pinto", "Red"),
    Car(CarMake.CHEVROLET, "Camaro", "Blue")
  )

  open fun findCars(filter: CarFilter): CompletableFuture<Cars> {
    return CompletableFuture.supplyAsync<Cars> {
      Cars(
        cars.filter {
          filter.make.isNullOrEmpty() || filter.make?.toLowerCase() == it.make.toString().toLowerCase()
        }.take(filter.limit),
        cars.size
      )
    }
  }
}

data class Car(val make: CarMake, val model: String, val color: String)
data class Cars(val cars: List<Car>, val total: Int)
data class CarFilter(val make: String?, val limit: Int, val offset: Int)

enum class CarMake {
  FORD, TOYOTA, CHEVROLET
}
