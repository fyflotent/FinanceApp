package controllers

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
  def getStockHistory(stockName: String) = Action {
    tryOrFail(() => Ok(Json.stringify(
      Json.toJson(stockInfoService.getStockHistory(stockName).map(e => e._1 -> e._2.high)))))
  }

  def getAllStocks() = Action {
    val stocksToGet = stockService.getStocks()
    val stockInfo = stockInfoService.getStocks(stocksToGet.toArray)
    val stockMap = stockInfo.map(entry => entry._1 -> entry._2.high)
    tryOrFail(() => Ok(Json.stringify(Json.toJson(stockMap))))
  }

  def getStockAverage(stockName: String) = Action {
    tryOrFail(() => Ok(s"$stockName ${stockInfoService.getStockAverage(stockName)}"))
  }

  def getStockAverages() = Action.async {
    val stocks = stockService.getStocks()
    stockInfoService.getStockAverages(stocks.toList).map(m => {
      //
      Ok(Json.stringify(Json.toJson(m)))
    })
  }
}
