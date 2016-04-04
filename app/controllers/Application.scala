package controllers

import play.api._
import play.api.mvc._
import play.twirl.api.Html

class Application extends Controller {

  var content = ""
  val gameresults = models.Scoreboard.getResults("04/03/2016")
  for(i <- 0 until  gameresults.size by 2){
    content += ("<div class=\"row\"> <div class=\"col-md-4\">" + gameresults(i).TEAM_ABBREVIATION.getOrElse("")+"</div> <div class=\"col-md-4\"> vs </div><div class=\"col-md-4\">"+gameresults(i+1).TEAM_ABBREVIATION.getOrElse("") + "</div></div>")
    content += ("<div class=\"row\"> <div class=\"col-md-4\">"+ gameresults(i).sumPoints+"</div> <div class=\"col-md-4\"> </div><div class=\"col-md-4\">"+gameresults(i+1).sumPoints + "</div></div>")
  }
  println(gameresults)

  def scoreboard = Action {
    Ok(views.html.scoreboard(Html(content)))
  }

}