package ControllerUtils

import play.api.libs.json._

case class HistoricalInfo(low: BigDecimal, high: BigDecimal)

case class StockInfo(price: BigDecimal, low: BigDecimal, high: BigDecimal)

object JsonConverters {

  implicit val stockInfoWrites: Writes[StockInfo] = (stockInfo: StockInfo) => Json.obj(
    "price" -> stockInfo.price,
    "low" -> stockInfo.low,
    "high" -> stockInfo.high
    )

  implicit val historicalInfoWrites: Writes[HistoricalInfo] = (stockInfo: HistoricalInfo) => Json
    .obj(
      "low" -> stockInfo.low,
      "high" -> stockInfo.high
      )

  implicit val errorMessageWrites: Writes[(String, String)] = (error: (String, String)) => Json.obj(
    error._1 -> error._2
    )

  implicit val eitherStringStockInfoWrites: Writes[Either[String, Map[String, StockInfo]]] = {
    case Left(str) =>
      JsString(str)
    case Right(siMap) =>
      Json.toJson(siMap)
  }
}
