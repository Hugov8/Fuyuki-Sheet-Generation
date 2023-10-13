package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import model.service.ServiceLocator
import model.data.GenerationSheetForm
import model.data.UpdateSheetForm
import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.Constraints._

import io.lemonlabs.uri._
import model.exception.ConnexionException

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SheetGenerationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with play.api.i18n.I18nSupport {
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`. 
   */

  val genForm = Form(mapping(
      "idWar" -> text,
      "mail" -> text
    )(GenerationSheetForm.apply)(GenerationSheetForm.unapply))

  val updateForm = Form(mapping(
    "urlSpreadsheet" -> text,
    "idWar" -> text
  )(UpdateSheetForm.apply)(UpdateSheetForm.unapply))

  def index = Action {implicit request: Request[AnyContent]=>{
    Ok(views.html.index(genForm, updateForm))
  }}

  def generateSheetForm = Action {implicit request: Request[AnyContent]=>{
    genForm.bindFromRequest().fold(
      formWithErrors =>{
        BadRequest(views.html.error(formWithErrors.toString()))
      },
      data =>{
        Redirect(routes.SheetGenerationController.generateSheet(data.idWar, data.mail))
      }
    )
  }}

  def generateSheet(idWar: String, shareToMail: String) = Action { implicit request: Request[AnyContent] => {
    try {
      val url = ServiceLocator.scriptService.generateWar(idWar, shareToMail)
      Redirect(routes.SheetGenerationController.result).flashing("message"->url, "state"->"200")
    } catch {
      case e: ConnexionException => Redirect(routes.SheetGenerationController.result).flashing("message"->e.message, "state"->"500")
      case e: Throwable => Redirect(routes.SheetGenerationController.result).flashing("message"->"Erreur inconnue", "state"->"500")
    }
  }}

  def updateSheetForm  = Action {implicit request: Request[AnyContent]=>{
    updateForm.bindFromRequest().fold(
      formWithErrors => {
        BadRequest(views.html.error(formWithErrors.toString()))
      },
      data => {
        val urlSheet: Url = AbsoluteUrl.parse(data.urlSpreadsheet)
        val idSpreadSheet = urlSheet.path.parts(2)
        Redirect(routes.SheetGenerationController.updateSheet(data.idWar, idSpreadSheet))
      }
    )
  }}

  def updateSheet(idWar: String, idSpreadsheet: String) = Action {
    implicit request: Request[AnyContent] => {
        try {
          val url = ServiceLocator.scriptService.updateWar(idWar, idSpreadsheet)
          Redirect(routes.SheetGenerationController.result).flashing("message"->url, "state"->"200")
        } catch {
          case e: ConnexionException => Redirect(routes.SheetGenerationController.result).flashing("message"->e.message, "state"->"500")
          case e: Throwable => Redirect(routes.SheetGenerationController.result).flashing("message"->"Erreur inconnue", "state"->"500")
        }
    }
  }

  def result = Action {
    implicit request: Request[AnyContent] => {
      request.flash.get("state").getOrElse("ko") match {
        case "200" => Ok(views.html.result(request.flash.get("message").getOrElse("")))
        case "500" => InternalServerError(views.html.error(request.flash.get("message").getOrElse("")))
        case _ => Ok(views.html.error(request.flash.get("message").getOrElse("")))
      }
      
    }
  }

}
