package ControllerUtils

import yahoofinance.histquotes.HistoricalQuote
import yahoofinance.quotes.stock.StockQuote
import yahoofinance.{Stock, YahooFinance}

import java.util.Calendar
import scala.jdk.CollectionConverters._

class ScalaQuote(quote: StockQuote) {
  def getPriceAvg200: BigDecimal = {
    BigDecimal(quote.getPriceAvg200)
  }

  def getPrice: BigDecimal = {
    BigDecimal(quote.getPrice)
  }

  def getDayLow: BigDecimal = {
    BigDecimal(quote.getDayLow)
  }

  def getDayHigh: BigDecimal = {
    BigDecimal(quote.getDayHigh)
  }
}

class ScalaCalendar(cal: Calendar) {
  def getTime = {
    cal.getTime
  }
}

class ScalaHistoricalQuote(quote: HistoricalQuote) {

  def getDate: ScalaCalendar = {
    new ScalaCalendar(quote.getDate)
  }

  def getLow: BigDecimal = {
    BigDecimal(quote.getLow)
  }

  def getHigh: BigDecimal = {
    BigDecimal(quote.getHigh)
  }
}

class ScalaStock(stock: Stock) {
  def getHistory: List[ScalaHistoricalQuote] = {
    stock.getHistory().asScala.toList.map(new ScalaHistoricalQuote(_))
  }

  def getQuote: ScalaQuote = {
    new ScalaQuote(stock.getQuote())
  }
}

//object ScalaYahooFinance extends ScalaYahooFinance

class ScalaYahooFinance {
  def get(stockName: String, refresh: Boolean = false): ScalaStock = {
    new ScalaStock(YahooFinance.get(stockName, refresh))
  }

  def getStockList(stockNames: Array[String], refresh: Boolean = false): Map[String, ScalaStock] = {
    YahooFinance.get(stockNames, refresh).asScala.toMap
      .map(entry => entry._1 -> new ScalaStock(entry._2))
  }
}
