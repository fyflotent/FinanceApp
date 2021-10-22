package services

import yahoofinance.YahooFinance
import yahoofinance.histquotes.HistoricalQuote
import yahoofinance.quotes.stock.StockQuote

import java.text.SimpleDateFormat
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._

case class HistoricalInfo(low: BigDecimal, high: BigDecimal)

case class StockInfo(price: BigDecimal, low: BigDecimal, high: BigDecimal)

class StockInfoService {
  val getStockHistory: String => Map[String, HistoricalInfo] = stockName => {
    val stockHistory = YahooFinance.get(stockName, true).getHistory().asScala.toList
    val t = stockHistory.map(
      quote =>
        dateFormat.format(quote.getDate.getTime) -> historicalQuoteToInfo(quote)
      )
    t.toMap
  }

  private val historicalQuoteToInfo: HistoricalQuote => HistoricalInfo = quote =>
    HistoricalInfo(quote.getLow, quote.getHigh)

  private val quoteToInfo: StockQuote => StockInfo = quote =>
    StockInfo(quote.getPrice, quote.getDayLow, quote.getDayHigh)

  private val dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss")

  def getStock(stockName: String): StockInfo = {
    quoteToInfo(YahooFinance.get(stockName).getQuote)
  }

  def getStocks(stockNames: Array[String]): Map[String, StockInfo] = {
    YahooFinance.get(stockNames, true)
      .asScala
      .toMap
      .map(entry => {
        val quote = entry._2.getQuote
        entry._1 -> quoteToInfo(quote)
      })
  }

  def getStockAverages(stockNames: List[String])(implicit executionContext: ExecutionContext) = {
    Future.sequence(stockNames.map(stockName => Future {
      stockName -> getStockAverage(stockName)
    })).map(list => list.toMap)
  }

  def getStockAverage(stockName: String) = {
    BigDecimal(YahooFinance.get(stockName).getQuote.getPriceAvg200)
  }
}
