package controllers

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import java.io.{BufferedWriter, File, FileWriter}

import play.api.data._
import play.api.data.Forms._
import play.api.data.Form
import play.api.mvc._
import play.twirl.api.Html

case class MatchDay(date: Date)

class Application extends Controller {
 val MatchDayForm = Form(
    mapping(
      "date" -> date
    )(MatchDay.apply)(MatchDay.unapply)
  )
  def search = Action { implicit request =>
    MatchDayForm.bindFromRequest.fold(
      formWithErrors => {
        val html = Html("ups")
        BadRequest(views.html.scoreboard(html)(MatchDayForm))
      },
      matchDayData => {
        val dateFormat = new SimpleDateFormat("MM-dd-yyyy")
        val today = dateFormat.format(matchDayData.date)
        Redirect("/"+today)
      }
    )
  }


  def scoreboard2(date: String) = Action {
    var content = ""
    val gameresults = models.Scoreboard.getResults(date.replace('-','/'))
    for(i <- 0 until  gameresults.size by 2){
      content += ("<div class=\"row\"> <div class=\"col-md-4\">" + gameresults(i).TEAM_ABBREVIATION.getOrElse("")+"</div> <div class=\"col-md-4\"> vs </div><div class=\"col-md-4\">"+gameresults(i+1).TEAM_ABBREVIATION.getOrElse("") + "</div></div>")
      content += ("<div class=\"row\"> <div class=\"col-md-4\">"+ gameresults(i).sumPoints+"</div> <div class=\"col-md-4\"> </div><div class=\"col-md-4\">"+gameresults(i+1).sumPoints + "</div></div>")
    }
    val html = Html(content)
    Ok(views.html.scoreboard(html)(MatchDayForm))
  }

  def scoreboard = Action {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
    val today = dateFormat.format(calendar.getTime)
    var content = ""
    val gameresults = models.Scoreboard.getResults(today)
    for(i <- 0 until  gameresults.size by 2){
      content += ("<div class=\"row\"> <div class=\"col-md-4\">" + gameresults(i).TEAM_ABBREVIATION.getOrElse("")+"</div> <div class=\"col-md-4\"> vs </div><div class=\"col-md-4\">"+gameresults(i+1).TEAM_ABBREVIATION.getOrElse("") + "</div></div>")
      content += ("<div class=\"row\"> <div class=\"col-md-4\">"+ gameresults(i).sumPoints+"</div> <div class=\"col-md-4\"> </div><div class=\"col-md-4\">"+gameresults(i+1).sumPoints + "</div></div>")
    }
    val html = Html(content)
    Ok(views.html.scoreboard(html)(MatchDayForm))
  }



}