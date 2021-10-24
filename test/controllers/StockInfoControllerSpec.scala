package controller

import ControllerUtils.{HistoricalInfo, StockInfo}
import controllers.StockInfoController
import org.mockito.ArgumentMatchers._
import org.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._
import services.{StockInfoService, StockService}

import scala.concurrent.ExecutionContext

class StockInfoControllerSpec
  extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {
  "StockInfoController#getStockHistory" should {
    "return stock history" in {
      val ec = inject[ExecutionContext]
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      when(mockStockInfoService.getStockHistory(anyString()))
        .thenReturn(Map("STCK" -> HistoricalInfo(1, 2)))
      val controller = new StockInfoController(
        stubControllerComponents(), mockStockInfoService, mockStockService)(ec)

      val result = controller.getStockHistory("STCK").apply(FakeRequest(GET, "/"))

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"STCK":{"low":1,"high":2}}"""
    }

    "return bad request if stock retrieval fails" in {
      val ec = inject[ExecutionContext]
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      when(mockStockInfoService.getStockHistory(anyString())).thenThrow(new Error("FAILURE"))
      val controller = new StockInfoController(
        stubControllerComponents(), mockStockInfoService, mockStockService)(ec)

      val result = controller.getStockHistory("STCK").apply(FakeRequest(GET, "/"))

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) mustBe "FAILURE"
    }
  }

  "StockInfoController#getAllStocks" should {
    "return all stocks stored by the users" in {
      val ec = inject[ExecutionContext]
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      when(mockStockService.getStocks()).thenReturn(Set("STCK"))
      when(mockStockInfoService.getStocks(any[Array[String]]()))
        .thenReturn(Map("STCK" -> StockInfo(1.5, 1, 2)))
      val controller = new StockInfoController(
        stubControllerComponents(), mockStockInfoService, mockStockService)(ec)

      val result = controller.getAllStocks().apply(FakeRequest(GET, "/"))

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"STCK":{"price":1.5,"low":1,"high":2}}"""
    }

    "return bad request if stock retrieval fails" in {
      val ec = inject[ExecutionContext]
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      when(mockStockService.getStocks()).thenReturn(Set("STCK"))
      when(mockStockInfoService.getStocks(any[Array[String]])).thenThrow(new Error("FAILURE"))
      val controller = new StockInfoController(
        stubControllerComponents(), mockStockInfoService, mockStockService)(ec)

      val result = controller.getAllStocks().apply(FakeRequest(GET, "/"))

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) mustBe "FAILURE"
    }
  }

  "StockInfoController#getStockAverage" should {
    "return all stocks stored by the users" in {
      val ec = inject[ExecutionContext]
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      when(mockStockInfoService.getStockAverage(anyString()))
        .thenReturn(10)
      val controller = new StockInfoController(
        stubControllerComponents(), mockStockInfoService, mockStockService)(ec)

      val result = controller.getStockAverage("STCK").apply(FakeRequest(GET, "/"))

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """{"average":10}"""
    }

    "return bad request if stock retrieval fails" in {
      val ec = inject[ExecutionContext]
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      when(mockStockInfoService.getStockAverage(anyString())).thenThrow(new Error("FAILURE"))
      val controller = new StockInfoController(
        stubControllerComponents(), mockStockInfoService, mockStockService)(ec)

      val result = controller.getStockAverage("STCK").apply(FakeRequest(GET, "/"))

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) mustBe "FAILURE"
    }
  }
}
