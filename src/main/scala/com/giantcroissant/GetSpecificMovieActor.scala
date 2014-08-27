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

import com.giantcroissant.GetSpecificMovieActor._

object GetSpecificMovieActor {
  case class GetSpecificMovieActorFromDBMessage(id: String)
  case class DoneGetSpecificMovieActorFromDBMessage(data: JsObject)
}

class GetSpecificMovieActor(implicit val mongoConnection: MongoConnection) extends Actor with ActorLogging {
  val dbActor = context.actorOf(Props(new GetSpecificMovieDBActor()))

  def receive = {
    case GetSpecificMovieRestMessage(result, id) => {
      log.info("GetSpecificMovieRestMessage")
      dbActor ! GetSpecificMovieActorFromDBMessage(id)
      context.become(waitSubActorDone)
    }
  }

  def waitSubActorDone: Receive = {
    case DoneGetSpecificMovieActorFromDBMessage(data) => {
      context.parent ! DoneGetSpecificMovieRestMessage()
    }
    case l:JsArray => {
      context.parent ! DoneGetSpecificMovieRestMessage(l)
    }
  }
}