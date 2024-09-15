package model.service

import model.connecteur.WarRequest
import model.data.Row
import model.connecteur.ScriptRequest
import model.connecteur.ScriptRequestRayshift
import model.connecteur.WarRequestRayshift
import model.data.War
import model.connecteur.sheets.SheetsServiceUtil
import model.connecteur.sheets.SpreadSheetUtil
import com.google.api.services.sheets.v4.model.Spreadsheet
import model.connecteur.sheets.UpdateSheetUtil
import model.connecteur.sheets.DriveUtil
import play.api.Logging
import model.connecteur.ScriptRequestAtlas
import model.parser.ParserAssembler

trait ScriptService extends Logging{
    val warRequester: WarRequest
    val scriptRequester: ScriptRequest
    def generateWar(idWar: String, mail: String): String
    def updateWar(idWar: String, idSpreadSheet: String): String
    def updateWar(idWar: String, idSpreadSheet: String, addSheet: Boolean): String
}

object ScriptServiceImpl extends ScriptService {
    override val scriptRequester: ScriptRequest = ScriptRequestRayshift
    override val warRequester: WarRequest = WarRequestRayshift
    override def generateWar(idWar: String, mail: String): String = {
        logger.info(s"Démarrage de la génération de la war $idWar pour $mail")
        val war: War = warRequester.getWarFromId(idWar)
        val spreadSheet: Spreadsheet = SpreadSheetUtil.createSpreadSheet(war.name)
        DriveUtil.shareSpreadsheet(spreadSheet.getSpreadsheetId(), mail)
        updateWar(idWar, spreadSheet.getSpreadsheetId(), true)
    }

    override def updateWar(idWar: String, idSpreadSheet: String, addSheet: Boolean = false): String = {
        logger.info(s"Démarrage de la mise à jour de la sheet $idSpreadSheet pour la war $idWar")
        val war: War = warRequester.getWarFromId(idWar)
        war.idRows.filter(_.id.substring(0,2)!="91").foreach(id => {
            val rowRayshift: Row = scriptRequester.getRowScript(id)
            val rowAtlas: Row = ScriptRequestAtlas.getRowScript(id)
            val row: Row = Row(rowRayshift.idRow, ParserAssembler.associateRayshiftAtlas(rowRayshift.lines, rowAtlas.lines))
            if(addSheet){
                UpdateSheetUtil.addSheet(row.idRow.id, idSpreadSheet)
            }
            UpdateSheetUtil.sendRow2Sheet(row, idSpreadSheet)
        })
        SheetsServiceUtil.baseURISheet+idSpreadSheet+"/edit"
    }

    override def updateWar(idWar: String, idSpreadSheet: String): String = updateWar(idWar, idSpreadSheet, false)
}