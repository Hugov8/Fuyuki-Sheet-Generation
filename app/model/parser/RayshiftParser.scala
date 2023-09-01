package model.parser

import model.data._

object RayshiftParser extends Parser[Line] {
    
    def parse(script: String): List[Line] = script.split("\n").map(_.split("\t")).map((x: Array[String]) => Line(x(0), x(1), x(2), getCharacter(x(3), x(4)), x(5), x(6))).toList

    def getCharacter(nameNA: String, nameJP: String): Character = nameNA match {
        case "1" => Master("1")
        case "2" => Master("2")
        case _ => NPC(nameNA, nameJP)
    }
    def unparse(res: List[Line]): String = 
        res.foldLeft("")( (acc, x) => {
            val character = x.character match {
                case x: NPC => x.nameNA+"\t"+x.nameJP
                case x: Master => x.value+"\t"+x.value
                case _ => "Unknown"+"\t"+"Unknown"
            }
            acc + x.idA+"\t"+x.idB+"\t"+x.idC+"\t"+character+ "\t" + x.speachNA + "\t" + x.speachJP + "\n"
        }).strip()
}