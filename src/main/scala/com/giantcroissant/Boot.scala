package com.giantcroissant

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

object Boot extends App {

  implicit val actorSystem = ActorSystem()

  val serviceActor = actorSystem.actorOf(
    Props[MainServiceActor],
    name = "MainServiceActor"
  )

  // server location and port should be read from some
  // configuration setting instead of hard-code here.
  IO(Http) ! Http.Bind(serviceActor, interface = "localhost", port = 8080)
}
