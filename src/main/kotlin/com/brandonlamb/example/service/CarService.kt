package com.brandonlamb.example.service

import org.apache.logging.log4j.LogManager
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Supplier
import javax.annotation.PreDestroy

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

  private val executor = Executors.newCachedThreadPool()

  @PreDestroy
  open fun preDestroy() {
    executor.awaitTermination(5, TimeUnit.SECONDS)
    LogManager.getLogger().info("Destroying location delivery dal")
  }

  open fun findCars(filter: CarFilter): CompletableFuture<Cars> {
    return CompletableFuture.supplyAsync<Cars>(Supplier {
      Cars(
        cars.filter {
          filter.make.isNullOrEmpty() || filter.make?.toLowerCase() == it.make.toString().toLowerCase()
        }.take(filter.limit),
        cars.size
      )
    }, executor)
  }
}

