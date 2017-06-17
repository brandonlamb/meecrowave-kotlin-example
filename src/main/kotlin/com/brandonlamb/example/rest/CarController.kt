package com.brandonlamb.example.rest

import com.brandonlamb.example.service.CarFilter
import com.brandonlamb.example.service.CarService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import org.apache.logging.log4j.LogManager
import java.net.HttpURLConnection
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Supplier
import javax.annotation.PreDestroy
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.validation.constraints.Max
import javax.ws.rs.DefaultValue
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

  private val executor = Executors.newCachedThreadPool()

  @PreDestroy
  open fun preDestroy() {
    executor.awaitTermination(5, TimeUnit.SECONDS)
    LogManager.getLogger().info("Destroying location delivery dal")
  }

  @GET
  @Path("ping")
  @Produces("application/text")
  @ApiOperation(response = String::class, httpMethod = "GET", value = "Get a ping response")
  @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Ping response")
  open fun getPing(): Response = Response.ok("pong").build()

  @GET
  @ApiOperation(value = "Get cars, optionally by make", httpMethod = "GET")
  @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Order import request successfully queued")
  open fun getCars(
    @ApiParam(name = "make", required = false, example = "Ford, Toyota")
    @qp("make")
    make: String?,

    @ApiParam(name = "limit", required = false, example = "10", defaultValue = "10")
    @qp("limit")
    @DefaultValue("10")
    @Max(value = 10, message = "1001")
    limit: Int,

    @ApiParam(name = "offset", required = false, example = "0", defaultValue = "0")
    @qp("offset")
    @DefaultValue("0")
    offset: Int,

    @Suspended res: AsyncResponse
  ) {
    res.setTimeout(10, TimeUnit.SECONDS)
    res.setTimeoutHandler { it.resume(Response.status(Response.Status.REQUEST_TIMEOUT).build()) }

    CompletableFuture.supplyAsync(Supplier {
      carService.findCars(CarFilter(make, limit, offset)).thenAccept {
        res.resume(Response.ok(Cars(
          it.cars.map { Car(it.make.toString().toLowerCase(), it.model, it.color) },
          Paging(it.total, limit, offset)
        )).build())
      }
    }, executor)
  }
}
