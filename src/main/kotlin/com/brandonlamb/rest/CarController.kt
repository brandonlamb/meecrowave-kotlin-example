package com.brandonlamb.rest

import com.brandonlamb.cars.domain.CarFilter
import com.brandonlamb.cars.commands.CarService
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import javax.validation.constraints.Max
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response
import com.brandonlamb.cars.domain.Car as CarDto
import javax.ws.rs.PathParam as p
import javax.ws.rs.QueryParam as qp

@Singleton
@Path("/cars")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
open class CarController @Inject constructor(
  private val carService: CarService,
  @Named("RestPool") private val es: ExecutorService
) {
  @GET
  open fun getCars(
    @qp("make") make: String?,
    @qp("limit") @DefaultValue("10") @Max(value = 10, message = "1001") limit: Int,
    @qp("offset") @DefaultValue("0") offset: Int
  ): CompletionStage<Response> {
    val cs = CompletableFuture<Response>()

    es.submit {
      carService.findCars(CarFilter(make, limit, offset)).thenAccept {
        cs.complete(Response.ok(Cars(
          it.cars.map { Car(it.make.toString().toLowerCase(), it.model, it.color) },
          Paging(it.total, limit, offset)
        )).build())
      }
    }

    return cs
  }
}
