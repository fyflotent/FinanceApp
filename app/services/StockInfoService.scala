package services

import ControllerUtils._

import java.text.SimpleDateFormat
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class StockInfoService @Inject()(private val scalaYahooFinance: ScalaYahooFinance) {
  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

  def getStockHistory(stockName: String): Map[String, HistoricalInfo] = {
    val stockHistory = scalaYahooFinance.get(stockName, refresh = true).getHistory
    val t = stockHistory.map(
      quote =>
        dateFormat.format(quote.getDate.getTime) -> historicalQuoteToInfo(quote)
      )
    t.toMap
  }

  private def historicalQuoteToInfo(quote: ScalaHistoricalQuote): HistoricalInfo = {
    HistoricalInfo(quote.getLow, quote.getHigh)
  }

  def getStock(stockName: String): StockInfo = {
    quoteToInfo(scalaYahooFinance.get(stockName).getQuote)
  }

  private def quoteToInfo(quote: ScalaQuote): StockInfo = {
    StockInfo(quote.getPrice, quote.getDayLow, quote.getDayHigh)
  }

  def getStocks(stockNames: Array[String]): Map[String, StockInfo] = {
    scalaYahooFinance.getStockList(stockNames, refresh = true)
      .map(entry => {
        val quote = entry._2.getQuote
        entry._1 -> quoteToInfo(quote)
      })
  }

  def getStockAverages(stockNames: List[String])
    (implicit executionContext: ExecutionContext): Future[Map[String, BigDecimal]] = {
    Future.sequence(stockNames.map(stockName => Future {
      stockName -> getStockAverage(stockName)
    })).map(list => list.toMap)
  }

  def getStockAverage(stockName: String): BigDecimal = {
    scalaYahooFinance.get(stockName).getQuote.getPriceAvg200
  }
}
