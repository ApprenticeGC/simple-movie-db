package com.giantcroissant

import akka.actor._
import akka.event.Logging

import spray.http._
import spray.httpx.PlayJsonSupport._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import spray.routing._

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import reactivemongo.api.MongoConnection
import reactivemongo.api.MongoDriver
import reactivemongo.api.Cursor

import com.giantcroissant.CreateMovieActor._

object CreateMovieActor {
  case class CreateMovieActorInDBMessage(data: JsObject)
  case class DoneCreateMovieActorInDBMessage(data: JsObject)
}

class CreateMovieActor(implicit val mongoConnection: MongoConnection) extends Actor with ActorLogging {
  val dbActor = context.actorOf(Props(new CreateMovieDBActor()))

  def receive = {
    case CreateMovieRestMessage(result, data) => {
      log.info("CreateMovieRestMessage")
      dbActor ! CreateMovieActorInDBMessage(data)
      context.become(waitSubActorDone)
    }
  }

  def waitSubActorDone: Receive = {
    case DoneCreateMovieActorInDBMessage(data) => {
      context.parent ! DoneCreateMovieRestMessage()
    }
    case l:JsArray => {
      context.parent ! DoneCreateMovieRestMessage(l)
    }
    case _ => {
      context.parent ! DoneCreateMovieRestMessage()      
    }
  }
}