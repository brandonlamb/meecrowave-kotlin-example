package com.brandonlamb.example

import org.pmw.tinylog.Configurator
import javax.annotation.PreDestroy
import javax.ejb.Startup
import javax.enterprise.context.ApplicationScoped

@Startup
@ApplicationScoped
open class Tinylog {
  @PreDestroy open fun destroy() = Configurator.shutdownWritingThread(true)
}
