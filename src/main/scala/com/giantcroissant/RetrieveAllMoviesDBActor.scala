package com.giantcroissant

import scala.concurrent.Future

import akka.actor._
import akka.event.Logging
import akka.pattern.{ask, pipe }

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

import play.modules.reactivemongo.json.collection.JSONCollection

import com.giantcroissant.RetrieveAllMoviesActor._

class RetrieveAllMoviesDBActor(implicit val mongoConnection: MongoConnection) extends Actor with ActorLogging {
  import scala.concurrent.ExecutionContext.Implicits.global
  def receive = {
    case RetrieveAllMoviesFromDBMessage() => {
      log.info("RetrieveAllMoviesFromDBMessage")
      val db = mongoConnection("o-john")
      val collection: JSONCollection = db.collection[JSONCollection]("movies")

      val cursor: Cursor[JsObject] = collection.
        // Find all without mathing any particular property
        find(Json.obj()).
        //find(Json.obj("name" -> "magic ball")).
        cursor[JsObject]

      log.info("done cursor fetching")

      val futureMovieList: Future[List[JsObject]] = cursor.collect[List]()
      val futureMovieJsonArray: Future[JsArray] = futureMovieList. map { movies =>
        Json.arr(movies)
      }

      futureMovieJsonArray pipeTo sender
    }
  }
}