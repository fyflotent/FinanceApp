package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import services.{StockInfoService, StockService, StockSocketServiceActor}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class StockSocketController @Inject()(
  cc: ControllerComponents, stockService: StockService,
  stockInfoService: StockInfoService
)(
  implicit system: ActorSystem,
  mat: Materializer,
  ec: ExecutionContext
) extends AbstractController(cc) {
  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      StockSocketServiceActor.props(out, stockService, stockInfoService)
    }
  }
}
