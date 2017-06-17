package com.brandonlamb.example.rest

import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application as App

@ApplicationPath("/")
open class Application : javax.ws.rs.core.Application()
