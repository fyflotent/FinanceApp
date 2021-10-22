package controllers

import ControllerUtils.ResultHelpers
import play.api.libs.json.JsValue
import play.api.mvc._
import services.{StockInfoService, StockService}

import javax.inject._

/**
 * This controller handles CRUD for a users stocks
 */
@Singleton
class StockController @Inject()(
  val controllerComponents: ControllerComponents,
  val stockService: StockService,
  val stockInfoService: StockInfoService
) extends BaseController {


  def getStocks() = Action {
    ResultHelpers.tryOrFail(() => Ok(stockService.getStocks().mkString(",")))
  }

  def postStocks() = Action { implicit request =>
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = body.asJson

    jsonBody map { json =>
      ResultHelpers.tryOrFail(
        () => {
          val newStock = (json \ "stock").as[String]
          println(s"Received new stock $newStock")
          stockInfoService.getStock(newStock) // Check if stock exists
          stockService.postStocks(newStock)
          Ok(s"Got $newStock")
        }, "Error setting stock")
    } getOrElse {
      BadRequest("Expected a json body")
    }
  }

  def deleteStocks(id: String) = Action { implicit request =>
    ResultHelpers.tryOrFail(() => {
      stockService.deleteStocks(id)
      Ok(s"Deleted $id")
    })
  }
}
