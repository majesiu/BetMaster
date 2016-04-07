package models

import java.io.{BufferedWriter, File, FileWriter}

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParseException
import play.api.libs.json.{JsPath, JsValue, Json, Reads}
import play.api.libs.functional.syntax._

import scalaj.http.Http

/**
  * Created by majesiu on 31.03.2016.
  */

object Scoreboard {

  case class GameResultRow(rowSet: List[GameResult])

  case class Team(name: List[String])

  case class Available(GAMEID: String, PT_AVAILABLE: Int) {
  }

  case class AvailableRow(name: String, headers: List[String], rowSet: List[Available]) {
  }

  case class GameResult(GAME_DATE_EST: Option[String], GAME_SEQUENCE: Option[Int], GAME_ID: Option[String], TEAM_ID: Option[Int], TEAM_ABBREVIATION: Option[String],
                        TEAM_WINS_LOSSES: Option[String], PTS_QTR1: Option[Int], PTS_QTR2: Option[Int], PTS_QTR3: Option[Int], PTS_QTR4: Option[Int],
                        PTS_OT1: Option[Int], PTS_OT2: Option[Int], PTS_OT3: Option[Int], PTS_OT4: Option[Int], PTS_OT5: Option[Int], PTS_OT6: Option[Int],
                        FG_PCT: Option[Double],FT_PCT: Option[Double], FG3_PCT: Option[Double], AST: Option[Int], REB: Option[Int], TOV: Option[Int]) {
    def sumPoints = {
      PTS_OT1.getOrElse(0) + PTS_OT2.getOrElse(0) + PTS_OT3.getOrElse(0) + PTS_QTR1.getOrElse(0) + PTS_QTR2.getOrElse(0) + PTS_QTR3.getOrElse(0) + PTS_QTR4.getOrElse(0) +
      PTS_OT4.getOrElse(0) + PTS_OT5.getOrElse(0) + PTS_OT6.getOrElse(0)
    }

  }

  def getScoreboardOnDay(date: String) = {
    val d2 = Http("http://stats.nba.com/stats/scoreboard/")
      .param("GameDate", date)
      .param("LeagueID", "00")
      .param("DayOffset", "0")
      .header("user-agent",
        """Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5)
               AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36""")
      .header("referer", "http://stats.nba.com/scores/")
      .asString.toString.split('(')
    val d = d2.drop(1).mkString("(").split('}')
    val data = d.take(d.length - 1).mkString("}").concat("}")
    val js = Json.parse(data)
    val file2 = new File("I:\\Users\\majesiu\\Desktop\\games.json")
    val bw2 = new BufferedWriter(new FileWriter(file2))
    bw2.write(Json.prettyPrint(js))
    bw2.close()
    js
  }

  implicit val availableReads: Reads[Available] = (
    JsPath(0).read[String] and
      JsPath(1).read[Int]
    ) (Available.apply _)

  implicit val availableRowReads: Reads[AvailableRow] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "headers").read[List[String]] and
      (JsPath \ "rowSet").read[List[Available]]
    ) (AvailableRow.apply _)

  implicit val gameResultReads: Reads[GameResult] = (
    JsPath(0).readNullable[String] and
      JsPath(1).readNullable[Int] and
      JsPath(2).readNullable[String] and
      JsPath(3).readNullable[Int] and
      JsPath(4).readNullable[String] and
      JsPath(6).readNullable[String] and
      JsPath(7).readNullable[Int] and
      JsPath(8).readNullable[Int] and
      JsPath(9).readNullable[Int] and
      JsPath(10).readNullable[Int] and
      JsPath(11).readNullable[Int] and
      JsPath(12).readNullable[Int] and
      JsPath(13).readNullable[Int] and
      JsPath(14).readNullable[Int] and
      JsPath(15).readNullable[Int] and
      JsPath(16).readNullable[Int] and
      JsPath(22).readNullable[Double] and
      JsPath(23).readNullable[Double] and
      JsPath(24).readNullable[Double] and
      JsPath(25).readNullable[Int] and
      JsPath(26).readNullable[Int] and
      JsPath(27).readNullable[Int]
    ) (GameResult.apply _)

  def getResults(date: String) = {
    val gameresults = ((((getScoreboardOnDay(date) \ "resultSets").get) (1).get) \ "rowSet").as[List[GameResult]]
    val file2 = new File("I:\\Users\\majesiu\\Desktop\\gameresults.json")
    val bw2 = new BufferedWriter(new FileWriter(file2))
    bw2.write(gameresults.mkString("\n"))
    bw2.close()
    gameresults
  }


  /*
    val datasets = (js \ "resultSets").get
    val games =  datasets(6).get
    val name = (games \ "headers").get

    println(Json.prettyPrint(name))

    val file = new File("I:\\Users\\majesiu\\Desktop\\datasets.json")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(Json.prettyPrint(datasets))
    bw.close()

    val datarowResult: JsResult[Datarow] = games.validate[Datarow]

    println(datarowResult.get)

    val file2 = new File("C:\\Users\\majesiu\\Desktop\\games.json")
    val bw2 = new BufferedWriter(new FileWriter(file2))
    bw2.write(Json.prettyPrint(games))
    bw2.close()*/
}
