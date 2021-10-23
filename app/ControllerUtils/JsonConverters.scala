package ControllerUtils

import play.api.libs.json._

case class HistoricalInfo(low: BigDecimal, high: BigDecimal)

case class StockInfo(price: BigDecimal, low: BigDecimal, high: BigDecimal)

object JsonConverters {
  implicit val stockInfoWrites = new Writes[StockInfo] {
    def writes(stockInfo: StockInfo) = Json.obj(
      "price" -> stockInfo.price,
      "low" -> stockInfo.low,
      "high" -> stockInfo.high
      )
  }

  implicit val historicalInfoWrites = new Writes[HistoricalInfo] {
    def writes(stockInfo: HistoricalInfo) = Json.obj(
      "low" -> stockInfo.low,
      "high" -> stockInfo.high
      )
  }

  implicit val errorMessage = new Writes[(String, String)] {
    def writes(error: (String, String)) = Json.obj(
      error._1 -> error._2
      )
  }
}
