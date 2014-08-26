package com.giantcroissant

import play.api.libs.json._

sealed trait RestMessage {
  def result: JsValue
}

case class RetrieveAllMoviesRestMessage(
  result: JsValue = Json.obj()
) extends RestMessage

case class DoneRetrieveAllMoviesRestMessage(
  result: JsValue = Json.obj()
) extends RestMessage

case class CreateMovieRestMessage(
   result: JsValue = Json.obj(),
   data: JsObject
) extends RestMessage

case class DoneCreateMovieRestMessage(
  result: JsValue = Json.obj()
) extends RestMessage
