package controllers

import play.api._
import play.api.mvc._
import play.twirl.api.Html

class Application extends Controller {

  var content = ""
  val gameresults = models.Scoreboard.getResults("03/31/2016")
  for(i <- 0 until  gameresults.size by 2){
    content += ("<p>" + gameresults(i).TEAM_ABBREVIATION.getOrElse("")+" vs "+gameresults(i+1).TEAM_ABBREVIATION.getOrElse("") + "</p>")
    content += ("<p>" + gameresults(i).sumPoints+" \t "+gameresults(i+1).sumPoints + "</p>")
  }

  def scoreboard = Action {
    Ok(views.html.scoreboard(Html(content)))
  }

}