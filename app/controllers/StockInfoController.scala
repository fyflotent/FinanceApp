package controllers

import play.api.libs.json._
import play.api.mvc._
import services.{StockInfoService, StockService}

import javax.inject._

@Singleton
class StockInfoController @Inject()(
  val controllerComponents: ControllerComponents,
  val stockInfoService: StockInfoService,
  val stockService: StockService
) extends BaseController {

  def getStockHistory(stockName: String) = Action {

    Ok(Json.stringify(Json.toJson(stockInfoService.getStockHistory(stockName))))
  }

  def getAllStocks() = Action {
    val stocksToGet = stockService.getStocks()
    val stockInfo = stockInfoService.getStocks(stocksToGet.toArray)
    val stockMap = stockInfo.map(entry => entry._1 -> BigDecimal(entry._2.getQuote.getDayHigh))
    Ok(Json.stringify(Json.toJson(stockMap)))
  }

  def getStockAverage() = Action[AnyContent] {
    NotImplemented("getStockAverage is not implemented yet")
  }
}
