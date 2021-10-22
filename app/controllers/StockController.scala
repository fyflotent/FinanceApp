package controllers

import play.api.libs.json.JsValue

import javax.inject._
import play.api.mvc._
import services.StockService
/**
 * This controller handles CRUD for a users stocks
 */
@Singleton
class StockController @Inject()(val controllerComponents: ControllerComponents, val stockService: StockService) extends BaseController {


  def getStocks() = Action {
    Ok(stockService.getStocks().mkString(","))
  }

  def postStocks() = Action { implicit request =>
    val body: AnyContent = request.body
    val jsonBody: Option[JsValue] = body.asJson

    jsonBody map { json =>
      val newStock = (json \ "stock").as[String]
      stockService.postStocks(newStock)
      Ok(s"Got $newStock")
    } getOrElse {
      BadRequest("Expected a json body")
    }
  }

  def deleteStocks(id: String) = Action { implicit request =>
    stockService.deleteStocks(id)
    Ok("Deleted")
  }
}
