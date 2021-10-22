package services

import akka.actor._
import play.api.libs.json.Json

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
            val stockMap = stockInfos.map(entry => entry._1 -> entry._2.price)
            out ! Json.stringify(Json.toJson(stockMap))
          } else {
            out ! "No Stocks set"
          }

        })
        cancellables = cancellables + cancellable
        out ! "Subscription Added"

      case "unsubscribe" =>
        cancellables.foreach(cancellable => cancellable.cancel())
        cancellables = Set()
        out ! "Cleared subscriptions"
    }
  }

}
