package services

import javax.inject._


@Singleton
class StockService {
  val getStocks = () => stocks

  val postStocks = (stockName: String) => {
    stocks = stocks + stockName
  }

  val deleteStocks = (stockName: String) => {
    stocks = stocks - stockName
  }

  @volatile private var stocks: Set[String] = Set()

}
