package com.giantcroissant

import akka.actor._
import akka.event.Logging

import spray.http._
import spray.httpx.PlayJsonSupport._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import spray.routing._

import play.api.libs.json._

import reactivemongo.api.MongoDriver

class MainServiceActor extends Actor with ActorLogging with MainService {
  def actorRefFactory = context

  def receive = runRoute(combinedRoute)
}

trait MainService extends HttpService with PerRequestActorCreator {
  this: Actor with ActorLogging =>

  implicit def mongoDriver = new MongoDriver(context.system)
  implicit def mongoConnection = mongoDriver.connection(List("localhost"))

  def retrieveAllMovies = {
    path("movies") {
      get { ctx =>
        val targetActorRef = Props(new RetrieveAllMoviesActor())
        perRequest(ctx, targetActorRef, RetrieveAllMoviesRestMessage())
      }
    }
  }

  def createMovie = {
    path("movies") {
      post {
        entity(as[JsObject]) { data => ctx =>
          val targetActorRef = Props(new CreateMovieActor())
          perRequest(ctx, targetActorRef, CreateMovieRestMessage(data = data))
        }
      }
    }
  }

  // def getSpecificMovie = {
  //   path("movies" / Segment) { movieId =>
  //     get { ctx =>
  //       ctx.complete(movieId)
  //     }
  //   }
  // }

  def combinedRoute =
    retrieveAllMovies ~
      createMovie// ~
      //getSpecificMovie
}