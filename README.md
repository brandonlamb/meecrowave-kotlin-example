# meecrowave-kotlin-example

Example maven project for Meecrowave using Kotlin

# What is it?

This is an example REST API app built on meecrowave and Kotlin, with support for OpenAPI Spec (Swagger).

# Install

Simply run `mvn clean package install meecrowave:run` to make sure things compile and run.

# The example REST application

This app is a simple Cars REST API exposing a `GET /cars?make={ford,toyota,chevrolet}` and `GET /cars/ping` endpoint.

There are really two main classes to look at:

- CarController
- CarService

Using CDI, CarService is injected into CarController, and both are `@ApplicationScoped`.

You may also notice the use of `AsyncResponse` for non-blocking behaviour so that the actual work happens off-thread.
