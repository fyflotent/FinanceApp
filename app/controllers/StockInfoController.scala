package controllers

import ControllerUtils.JsonConverters._
import ControllerUtils.ResultHelpers._
import play.api.libs.json._
import play.api.mvc._
import services.{StockInfoService, StockService}

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class StockInfoController @Inject()(
  val controllerComponents: ControllerComponents,
  val stockInfoService: StockInfoService,
  val stockService: StockService,
)(implicit ec: ExecutionContext) extends BaseController {

  def getStockHistory(stockName: String): Action[AnyContent] = Action {
    tryOrFail(() => Ok(
      Json.toJson(stockInfoService.getStockHistory(stockName))))
  }

  def getAllStocks: Action[AnyContent] = Action {
    tryOrFail(() => {
      val stocksToGet = stockService.getStocks()
      val stockInfo = stockInfoService.getStocks(stocksToGet.toArray)
      Ok(Json.toJson(stockInfo))
    })
  }

  def getStockAverage(stockName: String): Action[AnyContent] = Action {

    tryOrFail(() => {
      val num: BigDecimal = stockInfoService.getStockAverage(stockName)
      val map = Map("average" -> s"$num")
      Ok(Json.toJson(map))
    })
  }

  def getStockAverages: Action[AnyContent] = Action.async {
    val stocks = stockService.getStocks()
    stockInfoService.getStockAverages(stocks.toList).map(m => {
      Ok(Json.toJson(m))
    })
  }
}
