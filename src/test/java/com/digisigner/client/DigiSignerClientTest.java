package com.digisigner.client;

import com.digisigner.client.data.Document;
import com.digisigner.client.data.Field;
import com.digisigner.client.data.FieldType;
import com.digisigner.client.data.Signer;
import com.digisigner.client.requests.SignatureRequest;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public class DigiSignerClientTest {

    private static final String API_KEY = "78273487234";

    @Test
    public void testSendRequest() throws FileNotFoundException {

        // API client
        DigiSignerClient client = new DigiSignerClient(API_KEY);

        // signature request
        SignatureRequest signatureRequest = new SignatureRequest();

        // add document
        Document document1 = buildDocumentFromFile();
        signatureRequest.addDocument(document1);

        Document document2 = buildDocumentFromInputStream();
        signatureRequest.addDocument(document2);

        // send signature request
        try {
            SignatureRequest response = client.sendSignatureRequest(signatureRequest);
            System.out.println("Signature request id " + response.getId());
        } catch (DigiSignerException e) {  // in case http code is wrong
            System.out.println("Message: " + e.getMessage());
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
            String documentId = client.uploadDocument(document);
            System.out.println("Document ID = " + documentId);
        } catch (DigiSignerException e) {  // in case http code is wrong
            System.out.println("Message: " + e.getMessage());
        }
    }

    private Document buildDocumentFromFile() {

        URL url = getClass().getResource("/document.pdf");
        Document document = new Document(new File(url.getFile()), "fromJUNIT.pdf");

        // add signer to document
        Signer signer = new Signer("dmitry.lakhin@gmail.com");
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
        Signer signer = new Signer("dmitry.lakhin@gmail.com");
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
