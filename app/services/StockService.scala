package services

import javax.inject._


@Singleton
class StockService {
  @volatile private var stocks: Set[String] = Set()

  def postStocks(stockName: String) = {
    stocks = stocks + stockName
  }

  def deleteStocks(stockName: String) = {
    stocks = stocks - stockName
  }

  def getStocks() = {
    stocks
  }

}
