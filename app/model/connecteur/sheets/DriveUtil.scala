package model.connecteur.sheets


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest
import com.google.api.client.googleapis.json.GoogleJsonResponseException

import com.google.api.client.googleapis.batch.BatchRequest;


import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest
import model.exception.ConnexionException
import com.google.api.services.drive.model.Permission;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback
import com.google.api.client.http.HttpHeaders
import com.google.api.client.googleapis.json.GoogleJsonError
import play.api.Logger


object DriveUtil {
    val logger: Logger = Logger(this.getClass())
    val APPLICATION_NAME: String = "Fuyuki-Generation-Sheet"
    def getDriveService: Drive = {
        val credential = GoogleAuthorizeUtil.authorize
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build()
    }
    def shareSpreadsheet(idSpreadSheet: String, mail: String) = {
        val service: Drive = getDriveService
        val permission: Permission = new Permission().setType("user").setRole("writer").setEmailAddress(mail)
        val batch: BatchRequest = service.batch()
        val callback = new JsonBatchCallback[Permission]() {
        def onFailure(e: GoogleJsonError, responseHeaders: HttpHeaders) = {
            throw new ConnexionException(s"Le partage n'a pas abouti $e")
        }
        def onSuccess(permission: Permission, responseHeaders: HttpHeaders) = {
            logger.info(s"Permission ID:  ${permission.getId()} to $mail");
        }
    };

        service.permissions().create(idSpreadSheet, permission).setFields("id").queue(batch, callback)
        batch.execute()
    }
}