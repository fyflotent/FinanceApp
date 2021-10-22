package services

import yahoofinance.{Stock, YahooFinance}

import java.text.SimpleDateFormat
import scala.jdk.CollectionConverters._

class StockInfoService {

  val getStockHistory: String => Map[String, (BigDecimal, BigDecimal)] = (stockName) => {
    val stockHistory = getStock(stockName).getHistory().asScala.toList
    val t = stockHistory.map(
      quote =>
        dateFormat.format(quote.getDate.getTime) ->
          (BigDecimal(quote.getLow), BigDecimal(quote.getHigh))
      )
    t.toMap
  }

  private val dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss")

  def getStock(stockName: String): Stock = {
    YahooFinance.get(stockName, true)
  }

  def getStocks(stockNames: Array[String]): Map[String, Stock] = {
    YahooFinance.get(stockNames, true).asScala.toMap
  }
}
