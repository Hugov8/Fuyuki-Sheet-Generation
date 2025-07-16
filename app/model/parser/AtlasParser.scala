package model.parser

import model.data._
import model.data
import model.exception.ConnexionException
import model.exception.ParsingAtlasException
import org.slf4j.LoggerFactory

object AtlasParser extends Parser[Line] {
    val arobase: Char = '＠'
    val dollar: Char = '＄'
    val interrogation: Char = '？'
    val logger = LoggerFactory.getLogger(getClass)

    def parseMaster(script: List[String]): (List[Line], List[String]) = script match {
        case "？！" :: next => (List(), next)
        case head :: next => {
            (List(Line(NA(), NA(), NA(), Master(head.charAt(1).toString()), head.substring(3),"")), next)
        }
        case Nil => (List(), Nil)
    }

    def parseNPC(script: List[String], skipName: Boolean = false): (List[Line], List[String]) = script match {
        case "[k]" :: queue => (List(), queue)
        case name:: line :: next => {
            if(skipName) {
                val (res, restant) = parseNPC(line::next)
                (Line(NA(), NA(), NA(), NPC("/", ""), name.replace("[sr]", "[r]"), "") :: res, restant)
            } else {
                val (res, restant) = parseNPC(next)
                (Line(NA(), NA(), NA(), NPC(name.substring(1), ""), line.replace("[sr]", "[r]"), "") :: res, restant)
            }
        }
        case head :: Nil => throw new ParsingAtlasException(s"Erreur de parsing atlas $head")
        case Nil => (List(), Nil)
    }

    def parse(row: String): List[Line] = {
        val scriptLines: Array[String] = row.replace("\r", "").split("\n").map(i => {
            i match {
                case "" => i
                case "[k]" => i
                case i: String if i.charAt(0)==dollar => ""
                case i: String => i
            }
        }).filter(i=>(i!="" && (!"\\[[^\\]]*\\]".r.matches(i) || i == "[k]")))
        
        def recurseParse(script: List[String]): List[Line] = script match {
            case Nil => List()
            case script => {
                script(0).charAt(0) match {
                    case `arobase` => {
                        val (result, next) = parseNPC(script)
                        result ++ recurseParse(next)
                    }
                    case `interrogation` => {
                        val (result, next) = parseMaster(script)
                        result ++ recurseParse(next)
                    }
                    case _: Char => {
                        logger.warn(s"Attention ligne $script risque de décalage")
                        val (result, next) = parseNPC(script, true)
                        result ++ recurseParse(next)
                    }
                }
            }
        }

        recurseParse(scriptLines.toList)
    }
    def unparse(res : List[Line]): String = ???
}