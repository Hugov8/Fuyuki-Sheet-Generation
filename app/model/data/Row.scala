package model.data

import model.data.Line

case class Row(idRow: IdRow, lines: List[Line])
case class War(id: String, name: String, idRows: List[IdRow])
case class IdRow(id: String, scriptFileName: String)