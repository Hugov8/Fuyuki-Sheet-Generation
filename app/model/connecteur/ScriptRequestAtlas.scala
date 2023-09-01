package model.connecteur

import model.data.Row
import model.parser.AtlasParser

object ScriptRequestAtlas extends ScriptRequest {
  override val uri = "https://static.atlasacademy.io/NA/Script/"
  override val parser = AtlasParser
  override def buildURL(idRow: String): String = {
    val suffixe = idRow(0)
    suffixe match {
      case '9' => idRow.substring(0,2) match{
        case "94" => s"${uri}${idRow.substring(0,2)}/${idRow.substring(0, 4)}/$idRow.txt"
        case _ => s"${uri}${idRow.substring(0,2)}/$idRow.txt"
      } 
      case '0' => s"${uri}${idRow.substring(0,2)}/$idRow.txt"
      case _ => s"${uri}0$suffixe/0$idRow.txt"
    }
    
  }
}
