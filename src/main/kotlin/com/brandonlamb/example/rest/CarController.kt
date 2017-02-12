package com.brandonlamb.example.rest

import com.brandonlamb.example.service.CarFilter
import com.brandonlamb.example.service.CarService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import java.net.HttpURLConnection
import javax.ejb.Asynchronous
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response
import com.brandonlamb.example.service.Car as CarDto
import javax.ws.rs.PathParam as p
import javax.ws.rs.QueryParam as qp

@ApplicationScoped
@Path("/cars")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Api(
  value = "/cars",
  description = "Car Service",
  basePath = "/",
  protocols = "https",
  produces = "application/json",
  tags = arrayOf("cars")
)
open class CarController {
  @Inject
  private lateinit var carService: CarService

  @GET
  @Path("ping")
  @Produces("application/text")
  @ApiOperation(response = String::class, httpMethod = "GET", value = "Get a ping response")
  @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Ping response")
  open fun getPing(): Response = Response.ok("pong").build()

  @GET
  @ApiOperation(value = "Get cars, optionally by make", httpMethod = "GET")
  @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Order import request successfully queued")
  @Asynchronous
  open fun getCars(
    @ApiParam(name = "make", required = false, example = "Ford, Toyota")
    @javax.ws.rs.QueryParam("make")
    make: String?,

    @Suspended res: AsyncResponse
  ) {
    carService.findCars(CarFilter(make = make)).thenAccept {
      res.resume(Response.ok(Cars(
        it.cars.map { Car(it.make.toString().toLowerCase(), it.model, it.color) },
        Paging(it.total, 10, 0)
      )).build())
    }
  }
}

/**
 * Separate api and dto models to de-couple from service layer
 */
data class Car(val make: String, val model: String, val color: String)
data class Cars(val cars: List<Car>, val paging: Paging)
data class Paging(val total: Int, val limit: Int, val offset: Int)
