package com.brandonlamb.cars.commands

import com.brandonlamb.cars.domain.Car
import com.brandonlamb.cars.domain.CarFilter
import com.brandonlamb.cars.domain.CarMake
import com.brandonlamb.cars.domain.Cars
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.function.Supplier
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
open class CarService @Inject constructor(@Named("ServicePool") private val es: ExecutorService) {
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
    return CompletableFuture.supplyAsync<Cars>(Supplier {
      Cars(
        cars.filter {
          filter.make.isNullOrEmpty() || filter.make?.toLowerCase() == it.make.toString().toLowerCase()
        }.take(filter.limit),
        cars.size
      )
    }, es)
  }
}
