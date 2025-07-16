package model.connecteur.sheets

import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import model.exception.ConnexionException
import play.api.Logging
import com.google.api.services.sheets.v4.model.Sheet
import com.google.api.services.sheets.v4.model.SheetProperties
import java.{util => ju}

trait SpreadSheetUtilAbstractForm {
    val sheetService = SheetsServiceUtil.getSheetsService
    def createSpreadSheet(war: String): Spreadsheet
}

object SpreadSheetUtil extends SpreadSheetUtilAbstractForm with ExecutionSheet with Logging {
    override def createSpreadSheet(idWar: String): Spreadsheet = {
        val masterSheet = new Sheet().setProperties(new SheetProperties().setTitle(SheetsServiceUtil.MASTER_SHEET_NAME))
        val spreadSheet = new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle(idWar)).setSheets(ju.List.of(masterSheet))
        execute(sheetService.spreadsheets().create(spreadSheet)) match {
            case Some(x) => logger.info(s"Spreadsheet créé $x");x
            case None => throw new ConnexionException(s"Création échoué pour war = $idWar")
        }
    }
}