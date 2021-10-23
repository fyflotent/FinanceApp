package services

import ControllerUtils._
import org.mockito.ArgumentMatchers._
import org.mockito.MockitoSugar
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play._

import java.util.Date
import scala.concurrent.ExecutionContext.Implicits.global

class StockInfoServiceSpec extends PlaySpec with MockitoSugar with ScalaFutures {
  "StockService#getStockHistory" should {
    "return historical info for a stock" in {
      val mockYahooFinance = mock[ScalaYahooFinance]
      val mockScalaStock = mock[ScalaStock]
      val mockScalaHistorical = mock[ScalaHistoricalQuote]
      val mockScalaCalendar = mock[ScalaCalendar]
      when(mockYahooFinance.get(anyString(), anyBoolean())).thenReturn(mockScalaStock)
      when(mockScalaStock.getHistory).thenReturn(List(mockScalaHistorical))
      when(mockScalaHistorical.getHigh).thenReturn(10)
      when(mockScalaHistorical.getLow).thenReturn(8)
      when(mockScalaHistorical.getDate).thenReturn(mockScalaCalendar)
      when(mockScalaCalendar.getTime).thenReturn(new Date(1000000))

      val stockInfoService = new StockInfoService(mockYahooFinance)
      stockInfoService.getStockHistory("STCK") mustBe Map(
        "1969-12-31 07:16:40" -> HistoricalInfo(8, 10))
    }
  }

  "StockService#getStock" should {
    "return the a stock's quote information" in {
      val mockYahooFinance = mock[ScalaYahooFinance]
      val mockScalaStock = mock[ScalaStock]
      val mockScalaQuote = mock[ScalaQuote]
      when(mockYahooFinance.get(anyString(), anyBoolean())).thenReturn(mockScalaStock)
      when(mockScalaStock.getQuote).thenReturn(mockScalaQuote)
      when(mockScalaQuote.getDayHigh).thenReturn(10)
      when(mockScalaQuote.getDayLow).thenReturn(8)
      when(mockScalaQuote.getPrice).thenReturn(9)

      val stockInfoService = new StockInfoService(mockYahooFinance)
      stockInfoService.getStock("STCK") mustBe StockInfo(9, 8, 10)
    }
  }

  "StockService#getStocks" should {
    "return the a map of stock information" in {
      val mockYahooFinance = mock[ScalaYahooFinance]
      val mockScalaStock = mock[ScalaStock]
      val mockScalaQuote = mock[ScalaQuote]
      when(mockYahooFinance.getStockList(
        any[Array[String]],
        anyBoolean())).thenReturn(Map("STCK" -> mockScalaStock))
      when(mockScalaStock.getQuote).thenReturn(mockScalaQuote)
      when(mockScalaQuote.getDayHigh).thenReturn(10)
      when(mockScalaQuote.getDayLow).thenReturn(8)
      when(mockScalaQuote.getPrice).thenReturn(9)

      val stockInfoService = new StockInfoService(mockYahooFinance)
      stockInfoService.getStocks(Array("STCK")) mustBe Map("STCK" -> StockInfo(9, 8, 10))
    }
  }

  "StockService#getStockAverage" should {
    "return the a map of stock information" in {
      val mockYahooFinance = mock[ScalaYahooFinance]
      val mockScalaStock = mock[ScalaStock]
      val mockScalaQuote = mock[ScalaQuote]
      when(mockYahooFinance.get(anyString(), anyBoolean())).thenReturn(mockScalaStock)
      when(mockScalaStock.getQuote).thenReturn(mockScalaQuote)
      when(mockScalaQuote.getPriceAvg200).thenReturn(10)

      val stockInfoService = new StockInfoService(mockYahooFinance)
      stockInfoService.getStockAverage("STCK") mustBe (10)
    }
  }

  "StockService#getStockAverages" should {
    "return the a map of stock information" in {
      val mockYahooFinance = mock[ScalaYahooFinance]
      val mockScalaStock = mock[ScalaStock]
      val mockScalaQuote = mock[ScalaQuote]
      when(mockYahooFinance.get(anyString(), anyBoolean())).thenReturn(mockScalaStock)
      when(mockScalaStock.getQuote).thenReturn(mockScalaQuote)
      when(mockScalaQuote.getPriceAvg200).thenReturn(10)

      val stockInfoService = new StockInfoService(mockYahooFinance)
      whenReady(stockInfoService.getStockAverages(List("STCK"))) { result =>
        result mustBe Map("STCK" -> 10)
      }
    }
  }
}
