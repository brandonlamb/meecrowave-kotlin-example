package com.brandonlamb.infrastructure.threadpool

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy
import javax.enterprise.inject.Produces
import javax.inject.Named

open class ThreadPoolProducer {
  private val restPool = Executors.newCachedThreadPool()
  private val servicePool = Executors.newFixedThreadPool(5)

  @PreDestroy
  open fun preDestroy() {
    restPool.awaitTermination(5, TimeUnit.SECONDS)
    servicePool.awaitTermination(5, TimeUnit.SECONDS)
  }

  @Produces
  @Named("RestPool")
  open fun createRestPool(): ExecutorService = restPool

  @Produces
  @Named("ServicePool")
  open fun createServicePool(): ExecutorService = servicePool
}
