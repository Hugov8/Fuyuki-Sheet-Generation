package model.data

import model.data.Line

case class Row(idRow: String, lines: List[Line])
case class War(id: String, name: String, idRows: List[String])