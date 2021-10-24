package controllers

import ControllerUtils.StockInfo
import org.mockito.ArgumentMatchers._
import org.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import services.{StockInfoService, StockService}


class StockControllerSpec
  extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {
  "StockController#postStocks" should {
    "should post a new stock" in {
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      when(mockStockInfoService.getStock(anyString()))
        .thenReturn(StockInfo(2, 1, 3))
      val controller = new StockController(
        stubControllerComponents(), mockStockService, mockStockInfoService)

      val result = controller.postStocks()
        .apply(FakeRequest(POST, "/").withJsonBody(Json.parse("""{ "stock": "STCK" }""")))

      status(result) mustBe OK
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) mustBe "Got STCK"
    }

    "should fail if the stock doesn't exist" in {
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      when(mockStockInfoService.getStock(anyString()))
        .thenThrow(new Error("FAILURE"))

      val controller = new StockController(
        stubControllerComponents(), mockStockService, mockStockInfoService)

      val result = controller.postStocks()
        .apply(FakeRequest(POST, "/").withJsonBody(Json.parse("""{ "stock": "STCK" }""")))

      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) mustBe "FAILURE"
    }
  }

  "StockController#getStocks" should {
    "should retrieve a stock list" in {
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      when(mockStockService.getStocks()).thenReturn(Set("STCK"))
      val controller = new StockController(
        stubControllerComponents(), mockStockService, mockStockInfoService)

      val result = controller.getStocks.apply(FakeRequest(GET, "/"))

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) mustBe """["STCK"]"""
    }
  }

  "StockController#deleteStocks" should {
    "should retrieve a stock list" in {
      val mockStockInfoService = mock[StockInfoService]
      val mockStockService = mock[StockService]
      val controller = new StockController(
        stubControllerComponents(), mockStockService, mockStockInfoService)

      val result = controller.deleteStocks("STCK").apply(FakeRequest(GET, "/"))

      status(result) mustBe OK
      contentType(result) mustBe Some("text/plain")
      contentAsString(result) mustBe "Deleted STCK"
    }
  }
}
