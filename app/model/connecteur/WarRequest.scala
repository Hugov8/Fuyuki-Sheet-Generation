package model.connecteur

import model.exception._

import requests._
import play.api.libs.json._
import model.data.War
import model.data.IdRow

trait WarRequest {
    val uri: String
    def buildURL(idWar: String): String
    def getListIdRow(idWar: String): List[IdRow]
    def getNameRow(idWar: String): String
    def getResponse[T](idWar: String, accept: JsValue=>T): T = try {
        val response = requests.get(buildURL(idWar), readTimeout = 100_000)
        response.statusCode match {
            case 200 => accept(Json.parse(response.text()))
            case 404 => throw new NotExistException(response.text())
            case _ => throw new ConnexionException(s"Script request at $uri : ${response.text()}")
        }
    } catch  {
        case e: Throwable => throw new ConnexionException(e.toString())
    }

    def getWarFromId(id: String): War
}

object WarRequestRayshift extends WarRequest {
    val NONE = "\"NONE\""
    val acceptIdRows: JsValue=>List[IdRow] = (resultCall) => {
        val intro = (resultCall \ "mstWar" \"scriptId").get.toString
        //mstquest => [] allScripts => [] ScriptfileName
        val quests: List[IdRow] = (resultCall \ "mstQuest").get.as[List[JsValue]].map(x=>(x \ "allScripts" ).get.as[List[JsValue]])
            .filter(x => !x.isEmpty)
            .flatMap(x=>x.map(y => IdRow((y \ "questId").get.toString + (y \ "phase").get.toString + (y \ "sceneType").get.toString, (y \ "scriptFileName").get.toString)))
            //On enlève les "
            .map(s => IdRow(s.id, s.scriptFileName.substring(1, s.scriptFileName.length()-1)))
        intro match {
            case NONE => quests
            case _  => IdRow((resultCall \ "mstWar" \ "targetId").get.toString, intro.substring(1, intro.length()-1))::quests
        }
    }

    override val uri = "https://api.atlasacademy.io/raw/NA/war/"
    override def buildURL(idWar: String): String = uri+idWar
    override def getListIdRow(idWar: String): List[IdRow] = getResponse(idWar, acceptIdRows)
    override def getNameRow(idWar: String): String = getResponse(idWar, resultCall => (resultCall \ "mstWar" \ "longName").get.as[String])
    override def getWarFromId(id: String): War = War(id, getNameRow(id), getListIdRow(id))
}