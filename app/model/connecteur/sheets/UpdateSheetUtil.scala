package model.connecteur.sheets

import model.data.Line
import model.data.Row
import scala.jdk.CollectionConverters._
import com.google.api.services.sheets.v4.model.Sheet
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.api.services.sheets.v4.Sheets
import model.connecteur.sheets.SheetsServiceUtil
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import com.google.api.services.sheets.v4.model.Request
import com.google.api.services.sheets.v4.model.AddSheetRequest
import com.google.api.services.sheets.v4.model.SheetProperties
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest
import java.util.Arrays
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest
import model.exception.ConnexionException
import play.api.Logger

trait UpdateSheetUtilAbstractForm[T] extends ExecutionSheet{
    val logger: Logger = Logger(this.getClass())
    val sheetService: Sheets = SheetsServiceUtil.getSheetsService
    def sendRow2Sheet(row: T, sheetId: String): BatchUpdateValuesResponse
    def addSheet(title: String, spreadsheetId: String) = {
        val requests = List(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle(title))))
        logger.info(s"Création de la sheet $title dans la spreadsheet $spreadsheetId")
        execute(sheetService.spreadsheets().batchUpdate(spreadsheetId, new BatchUpdateSpreadsheetRequest().setRequests(requests.asJava)))
    }
}

object UpdateSheetUtil extends UpdateSheetUtilAbstractForm[Row] {
    override def sendRow2Sheet(row: Row, spreadsheetId: String): BatchUpdateValuesResponse = {
        val letter: String = "H"
        val number: Int = 3 + row.lines.length
        val sheetName: String = "'"+row.idRow+"'!"
        val rangeScript: String = "'"+row.idRow+"'!A3:"+letter+number
        val data: List[ValueRange] = List(new ValueRange().setRange(sheetName+"A1").setValues(Arrays.asList(Arrays.asList(row.idRow.asInstanceOf[Object]))), 
            new ValueRange().setRange(sheetName+"D2").setValues(Arrays.asList(Arrays.asList("ENName", "JPName", "English", "Japanese", "French", "Character limit"))),
            row.setRange(rangeScript))
        val batchBody: BatchUpdateValuesRequest = new BatchUpdateValuesRequest().setValueInputOption("USER_ENTERED").setData(data.asJava);
        
        execute(sheetService.spreadsheets().values().batchUpdate(spreadsheetId, batchBody)) match {
            case Some(x) => x
            case None => throw new ConnexionException(s"SendRow2Sheet a échoué avec row = $row\n et spreadsheet = $spreadsheetId")
        }
    }

    implicit def row2ValueRange(row:Row): ValueRange = new ValueRange().setValues(row.lines.map(_.getListOfString.map(_.asInstanceOf[Object]).asJava).asJava)
}