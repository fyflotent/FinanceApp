package ControllerUtils

import play.api.mvc.Result
import play.api.mvc.Results.BadRequest

import scala.util.{Failure, Success, Try}

object ResultHelpers {
  def tryOrFail(doFunc: () => Result, errorString: String = "Unknown error"): Result = {
    Try(doFunc()) match {
      case Success(value) => value
      case Failure(error) =>
        BadRequest(Option(error.getMessage).getOrElse(errorString))
    }
  }
}
