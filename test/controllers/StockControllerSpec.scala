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

class StockInfoControllerSpec
  extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {
  "StockController#getStocks" should {
    "should retrieve a stock list" in {
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
}
