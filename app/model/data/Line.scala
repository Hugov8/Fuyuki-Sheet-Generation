package model.data

case class Line(idA: IdLineOrNA, idB: IdLineOrNA, idC: IdLineOrNA, character: Character, speachNA: String, speachJP: String) {
    def getListOfString: List[String] = {
        val nameNa = character match {
            case x: NPC => x.nameNA
            case x: Master => x.value
        }
        val nameJP = character match {
            case x: NPC => x.nameJP
            case x: Master => x.value
        }
        idA match {
            case _: IdLine => List(idA, idB, idC, nameNa, nameJP, speachNA, speachJP)
            case _: NA => List(nameNa, nameJP, speachNA, speachJP)
        }
    }
}

abstract class IdLineOrNA 
object IdLineOrNA{
    implicit def string2IdLine(s: String): IdLine = IdLine(s)
    implicit def idLine2String(idLine: IdLineOrNA): String = idLine match {
        case IdLine(id) => id
        case _: IdLineOrNA => ""
    }
}

case class IdLine(id: String) extends IdLineOrNA
case class NA() extends IdLineOrNA

abstract class Character
case class Master(val value: String) extends Character
case class NPC(val nameNA: String, val nameJP: String) extends Character
