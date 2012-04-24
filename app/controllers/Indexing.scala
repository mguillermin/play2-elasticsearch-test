package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import index.IndexUtils
import index.Document

/**
 * Indexing controller : everything related to the indexing
 */
object Indexing extends Controller {

  case class Generate(nb: Int)

  val generateForm = Form(
    mapping(
      "nb" -> (number(min=1, max=10000))
    )(Generate.apply)(Generate.unapply)
  )

  /**
   * Displays the form used to generate documents
   * @return
   */
  def generate = Action { implicit request =>
    Ok(views.html.indexing.generate(generateForm)(Nil))
  }

  /**
   * Process the form and generate documents
   * @return
   */
  def generateData = Action { implicit request =>
    generateForm.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.indexing.generate(formWithErrors)(List("Validation error")))
      },
      data => {
        for(i <- 1.to(data.nb)) {
          IndexUtils.get().index(Document.randomDocument());
        }
        Redirect(routes.Indexing.generate).flashing(
          flash + ("success" -> "%d document(s) have been generated".format(data.nb))
        )
      }
    )
  }
}