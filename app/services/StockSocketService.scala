package services

import ControllerUtils.StockInfo
import akka.actor._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object StockSocketServiceActor {
  def props(out: ActorRef, stockService: StockService, stockInfoService: StockInfoService)
    (implicit system: ActorSystem, ec: ExecutionContext) =
    Props(new StockSocketServiceActor(out, stockService, stockInfoService))
}

class StockSocketServiceActor(
  out: ActorRef,
  stockService: StockService,
  stockInfoService: StockInfoService
)
  (implicit system: ActorSystem, ec: ExecutionContext) extends Actor {

  var cancellables: Set[Cancellable] = Set()

  def receive = {
    case msg: String => msg match {
      case "subscribe" =>
        val cancellable = system.scheduler.scheduleWithFixedDelay(0.seconds, 10.seconds)(() => {
          val stocks = stockService.getStocks()
          if (stocks.nonEmpty) {
            val stockInfos = stockInfoService.getStocks(stocks.toArray)
            val stockMap = stockInfos
            out ! Right(stockMap)
          } else {
            out ! Right(Map[String, StockInfo]())
          }

        })
        cancellables = cancellables + cancellable
        out ! Left("Subscription Added")

      case "unsubscribe" =>
        cancellables.foreach(cancellable => cancellable.cancel())
        cancellables = Set()
        out ! Left("Cleared subscriptions")
      case m => {
        println(s"Unrecognized message $m")
      }
    }
  }

}
