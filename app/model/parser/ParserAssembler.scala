package model.parser

import model.data.Line
import model.exception.ParsingAssemblerException

object ParserAssembler {
    def associateRayshiftAtlas(rayshift: Line, atlas: Line): Line = Line(rayshift.idA, rayshift.idB, rayshift.idC, rayshift.character, atlas.speachNA, rayshift.speachJP)
    def associateRayshiftAtlas(rayshift: List[Line], atlas: List[Line]): List[Line] = (rayshift, atlas) match {
        //Dans le cas où les scripts ne génèrent pas les mêmes objets
        case (x,y) if x.length != y.length => x
        case (x1::y1, x2::y2) => associateRayshiftAtlas(x1, x2) :: associateRayshiftAtlas(y1, y2)
        case (Nil, Nil) => Nil
        case (Nil, x) => println(s"Atlas : $x");Nil
        case (x, Nil) => println(s"rayshift : $x");x
        case _ => throw new ParsingAssemblerException(s"Mauvais parsing entre rayshift ($rayshift) et atlas ($atlas)")
    }
}