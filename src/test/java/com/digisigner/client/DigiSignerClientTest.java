package com.digisigner.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.digisigner.client.data.Document;
import com.digisigner.client.data.Field;
import com.digisigner.client.data.FieldType;
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.data.Signer;

/**
 * The JUnit class for the DigiSigner client.
 */
public class DigiSignerClientTest {

    /**
     * The api key for test.
     * The value can be found in the DigiSigner account (Settings dialog).
     */
    private static final String API_KEY = "YOUR_API_KEY";

    private static final String TEST_SIGNER_EMAIL = "asdf.asdf@list.ru";

    private static final String MESSAGE = "Message: ";

    @Test
    public void testSendRequest() throws FileNotFoundException {

        // API client
        DigiSignerClient client = new DigiSignerClient(API_KEY);

        // signature request
        SignatureRequest signatureRequest = new SignatureRequest();


        try {
            // add document
            Document document1 = buildDocumentFromFile();
            signatureRequest.addDocument(document1);
            signatureRequest.setEmbedded(true);
            signatureRequest.setRedirectForSigningToUrl("www.encoded.com");

            Document document2 = buildDocumentFromInputStream();
            signatureRequest.addDocument(document2);

            // send signature request
            SignatureRequest response = client.sendSignatureRequest(signatureRequest);
            System.out.println("Signature request id " + response.getSignatureRequestId());
        } catch (DigiSignerException e) {  // in case http code is wrong
            System.out.println(MESSAGE + e.getMessage());
            for (String error : e.getErrors()) {
                System.out.println(error);
            }
        }
    }

    @Test
    public void testGetSignatureRequest() throws FileNotFoundException {

        // API client
        DigiSignerClient client = new DigiSignerClient(API_KEY);

        // signature request to send
        SignatureRequest signatureRequestToSend = new SignatureRequest();


        try {
            signatureRequestToSend.setSendEmails(true);
            signatureRequestToSend.setRedirectForSigningToUrl("http://www.encoded.com?a=b");
            signatureRequestToSend.setEmbedded(true);
            // add document
            Document document1 = buildDocumentFromFile();
            signatureRequestToSend.addDocument(document1);

            Document document2 = buildDocumentFromInputStream();
            signatureRequestToSend.addDocument(document2);

            // send signature request
            SignatureRequest response = client.sendSignatureRequest(signatureRequestToSend);
            String signatureRequestId = response.getSignatureRequestId();
            System.out.println("Signature request id " + signatureRequestId);

            // get signature request ID
            SignatureRequest result = client.getSignatureRequest(signatureRequestId);
            assertEquals("The signature request has wrong signature ID", signatureRequestId,
                    result.getSignatureRequestId());

            String urlToSignFirstDocument = result.getDocuments().get(0).getSigners().get(0).getSignDocumentUrl();
            System.out.println(urlToSignFirstDocument);

        } catch (DigiSignerException e) {  // in case http code is wrong
            System.out.println(MESSAGE + e.getMessage());
            for (String error : e.getErrors()) {
                System.out.println(error);
            }
        }
    }

    @Test
    public void testUploadDocument() throws FileNotFoundException {
        // API client
        DigiSignerClient client = new DigiSignerClient(API_KEY);

        // add document
        Document document = buildDocumentFromFile();

        try {
            // call upload
            System.out.println("Document ID = " + client.uploadDocument(document).getId());
        } catch (DigiSignerException e) {  // in case http code is wrong
            System.out.println(MESSAGE + e.getMessage());
        }
    }

    @Test
    public void testUploadAndGetDocument() throws FileNotFoundException {
        // API client
        DigiSignerClient client = new DigiSignerClient(API_KEY);

        // add document
        Document document = buildDocumentFromFile();

        try {
            // call upload
            String documentId = client.uploadDocument(document).getId();
            System.out.println("Document ID = " + documentId);

            // get file
            Document retrievedDocument = client.getDocumentById(documentId, "newFileName.pdf");
            System.out.println("The downloaded filePath = " + retrievedDocument.getFile().getAbsolutePath());
        } catch (DigiSignerException e) {  // in case http code is wrong
            System.out.println(MESSAGE + e.getMessage());
        }
    }

    private Document buildDocumentFromFile() {

        URL url = getClass().getResource("/document.pdf");
        Document document = new Document(new File(url.getFile()), "fromJUNIT.pdf");

        // add signer to document
        Signer signer = new Signer(TEST_SIGNER_EMAIL);
        signer.setRole("manager");
        document.addSigner(signer);

        int[] rectangle1 = new int[]{0, 0, 200, 100};
        Field field1 = new Field(0, rectangle1, FieldType.SIGNATURE);
        field1.setRequired(false);
        signer.addField(field1);

        int[] rectangle2 = new int[]{300, 0, 500, 100};
        Field field2 = new Field(0, rectangle2, FieldType.TEXT);
        field2.setRequired(false);
        signer.addField(field2);
        return document;
    }

    private Document buildDocumentFromInputStream() throws FileNotFoundException {
        URL url = getClass().getResource("/document.pdf");
        InputStream inputStream = new FileInputStream(new File(url.getFile()));
        Document document1 = new Document(inputStream, "test.pdf");

        // add signer to document
        Signer signer = new Signer(TEST_SIGNER_EMAIL);
        signer.setRole("client");
        document1.addSigner(signer);

        int[] rectangle1 = new int[]{0, 0, 200, 100};
        Field field1 = new Field(0, rectangle1, FieldType.SIGNATURE);
        field1.setRequired(false);
        signer.addField(field1);

        int[] rectangle2 = new int[]{300, 0, 500, 100};
        Field field2 = new Field(0, rectangle2, FieldType.TEXT);
        field2.setRequired(false);
        signer.addField(field2);

        return document1;
    }
}
