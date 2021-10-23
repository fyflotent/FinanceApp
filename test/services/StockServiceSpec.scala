package services

import org.scalatestplus.play._

class StockServiceSpec extends PlaySpec {
  "StockService" should {
    "add a stock" in {
      val stockService = new StockService
      val stock = "Stock"
      stockService.postStocks(stock)
      stockService.getStocks() mustBe Set(stock)
    }

    "delete a stock" in {
      val stockService = new StockService
      val stock = "Stock"
      stockService.postStocks(stock)
      stockService.getStocks() mustBe Set(stock)
      stockService.deleteStocks(stock)
      stockService.getStocks() mustBe Set()
    }
  }
}
