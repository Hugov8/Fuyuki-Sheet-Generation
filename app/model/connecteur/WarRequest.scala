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
    def getResponse[T](idWar: String, accept: JsValue=>T): T = {
        val response = requests.get(buildURL(idWar))
        response.statusCode match {
            case 200 => accept(Json.parse(response.text()))
            case 404 => throw new NotExistException(response.text())
            case _ => throw new ConnexionException(s"Script request at $uri : ${response.text()}")
        }
    }
    def getWarFromId(id: String): War
}

object WarRequestRayshift extends WarRequest {

    val acceptIdRows: JsValue=>List[String] = (resultCall) => {
        val intro = (resultCall \ "mstWar" \"targetId").get.toString
        //mstquest => [] allScripts => [] ScriptfileName
        val quests: List[String] = (resultCall \ "mstQuest").get.as[List[JsValue]].map(x=>(x \ "allScripts" ).get.as[List[JsValue]])
            .filter(x => !x.isEmpty).flatMap(x=>x.map(y => (y \ "questId").get.toString + (y \ "phase").get.toString + (y \ "sceneType").get.toString))
        intro match {
            case "0" => quests
            case _ => intro::quests
        }
    }

    override val uri = "https://api.atlasacademy.io/raw/NA/war/"
    override def buildURL(idWar: String): String = uri+idWar
    override def getListIdRow(idWar: String): List[String] = getResponse(idWar, acceptIdRows)
    override def getNameRow(idWar: String): String = getResponse(idWar, resultCall => (resultCall \ "mstWar" \ "longName").get.as[String])
    override def getWarFromId(id: String): War = War(id, getNameRow(id), getListIdRow(id))
}