package model.connecteur.sheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest
import com.google.api.client.googleapis.json.GoogleJsonResponseException

import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest
import model.exception.ConnexionException

object SheetsServiceUtil {
    val APPLICATION_NAME: String = "Fuyuki-Generation-Sheet"
    val baseURISheet: String = "https://docs.google.com/spreadsheets/d/"
    def getSheetsService: Sheets  = {
        val credential = GoogleAuthorizeUtil.authorize
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build()
    }
}

trait ExecutionSheet {
    
    def execute[T](batch: AbstractGoogleClientRequest[T]): Option[T] = {
        try{
            Some(batch.execute())
        } catch {
            case e: GoogleJsonResponseException => {
                e.getStatusCode() match {
                    case 400 => throw new ConnexionException(s"Erreur API Google sheet. Veuillez vérifier le lien fourni et l'id de la war\n${e.getContent()}")
                    case 429 => Thread.sleep(60_000);execute(batch)
                    case _ => throw new ConnexionException(e.getContent())
                }
            }
            case e: Throwable => println(e);throw e;
        }
    }
}