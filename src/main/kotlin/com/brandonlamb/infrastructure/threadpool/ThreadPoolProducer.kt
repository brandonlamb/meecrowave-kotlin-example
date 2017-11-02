package com.brandonlamb.infrastructure.threadpool

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import javax.enterprise.inject.Produces
import javax.inject.Named

class ThreadPoolProducer {
  private val restPool = Executors.newCachedThreadPool()
  private val servicePool = Executors.newFixedThreadPool(5)

  @PreDestroy
  fun preDestroy() {
    restPool.awaitTermination(5, TimeUnit.SECONDS)
    servicePool.awaitTermination(5, TimeUnit.SECONDS)
  }

  @Produces
  @Named("RestPool")
  fun createRestPool(): ExecutorService = restPool

  @Produces
  @Named("ServicePool")
  fun createServicePool(): ExecutorService = servicePool
}