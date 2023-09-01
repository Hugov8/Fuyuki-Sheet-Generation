package model

import org.scalatest.flatspec.AnyFlatSpec
import model.connecteur.ScriptRequestRayshift
import model.connecteur.ScriptRequestAtlas
import model.connecteur.sheets.SheetsServiceUtil
import model.connecteur.sheets.SpreadSheetUtil

import model.data._
import model.parser.RayshiftParser
import model.connecteur.WarRequestRayshift
import model.exception.ApplicationException
import com.google.api.services.sheets.v4.model.ValueRange

import java.util.Arrays
import model.connecteur.sheets.UpdateSheetUtil

class SheetConnexionServiceTest extends AnyFlatSpec {
    val SPREADSHEET_ID = sys.env.get("SPREADSHEET_ID") match {
        case Some(value) => value
        case None => throw new ApplicationException("SPREADSHEET_ID not found")
    }
    "Connexion" should "succces" in {
        val sheetService = SheetsServiceUtil.getSheetsService
        val body : ValueRange = new ValueRange().setValues(Arrays.asList(
        Arrays.asList("Expenses January"), 
        Arrays.asList("books", "30"), 
        Arrays.asList("pens", "10"),
        Arrays.asList("Expenses February"), 
        Arrays.asList("clothes", "20"),
        Arrays.asList("shoes", "5")));

        sheetService.spreadsheets().values().update(SPREADSHEET_ID, "A1", body).setValueInputOption("RAW").execute();
    }

    "Row" should "send to sheet" in {
        val row: Row = ScriptRequestRayshift.getRowScript("100072161")
        UpdateSheetUtil.addSheet(row.idRow, SPREADSHEET_ID)
        UpdateSheetUtil.sendRow2Sheet(row, SPREADSHEET_ID)
    }

    "Spreadsheet" should "be created" in {
        assert(SpreadSheetUtil.createSpreadSheet("107").getSpreadsheetId()!=null)
    }
}