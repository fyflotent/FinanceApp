package controllers

import ControllerUtils.JsonConverters._
import ControllerUtils.StockInfo
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
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
  implicit val messageFlowTransformer = MessageFlowTransformer
    .jsonMessageFlowTransformer[String, Either[String, Map[String, StockInfo]]]

  def socket = WebSocket.accept[String, Either[String, Map[String, StockInfo]]] { request =>
    ActorFlow.actorRef { out =>
      StockSocketServiceActor.props(out, stockService, stockInfoService)
    }
  }
}
