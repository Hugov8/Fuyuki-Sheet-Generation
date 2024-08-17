package model.connecteur

import model.exception._

import requests._
import play.api.libs.json._
import model.data.War

trait WarRequest {
    val uri: String
    def buildURL(idWar: String): String
    def getListIdRow(idWar: String): List[String]
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
    val acceptIdRows: JsValue=>List[String] = (resultCall) => {
        val intro = (resultCall \ "mstWar" \"scriptId").get.toString
        //mstquest => [] allScripts => [] ScriptfileName
        val quests: List[String] = (resultCall \ "mstQuest").get.as[List[JsValue]].map(x=>(x \ "allScripts" ).get.as[List[JsValue]])
            .filter(x => !x.isEmpty).flatMap(x=>x.map(y => (y \ "scriptFileName").get.toString)).map(s => s.substring(1, s.length()-1))
            .map(s=> s match {
                case x if x.charAt(0)=='0' && x.charAt(1) =='1' => x.substring(1)
                case _: String => s
            })
        intro match {
            case NONE => quests
            case _ if intro.charAt(1)=='0' && intro.charAt(2) =='1' => intro.substring(2, intro.length()-1)::quests
            case _  => intro.substring(1, intro.length()-1)::quests
        }
    }

    override val uri = "https://api.atlasacademy.io/raw/NA/war/"
    override def buildURL(idWar: String): String = uri+idWar
    override def getListIdRow(idWar: String): List[String] = getResponse(idWar, acceptIdRows)
    override def getNameRow(idWar: String): String = getResponse(idWar, resultCall => (resultCall \ "mstWar" \ "longName").get.as[String])
    override def getWarFromId(id: String): War = War(id, getNameRow(id), getListIdRow(id))
}