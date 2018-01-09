package com.digisigner.signaturerequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

import com.digisigner.client.DigiSignerClient;
import com.digisigner.client.data.Document;
import com.digisigner.client.data.DocumentFields;
import com.digisigner.client.data.Field;
import com.digisigner.client.data.FieldType;
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.data.Signer;

/**
 * SignatureRequestSimpleTest demonstrates and tests the most simple API usage,
 * one signer, only required attributes are set.
 */
public class SignatureRequestSimpleTest extends SignatureRequestTest {

    // API client
    private DigiSignerClient client = new DigiSignerClient(SERVER_URL, API_KEY);

    /**
     * Tests send signature request for file.
     * Example string for curl:
     * {"send_emails": false,
     *   "documents" : [
     *     {"document_id": "f1cd390a-70cd-4b9e-9b11-288016e9985f", "signers": [{"email": "signer_1@example.com"}]}]}
     */
    @Test
    public void testSendSignatureRequestForFile() {

        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setSendEmails(SEND_EMAILS);

        // add document from file and one signer
        URL url = getClass().getResource(TEST_DOCUMENT_LOCATION);
        Document document = new Document(new File(url.getFile()), "TestSendSignatureRequestForFile.pdf");
        document.addSigner(new Signer(SIGNER_EMAIL[0]));
        signatureRequest.addDocument(document);

        // execute signature request
        SignatureRequest signatureRequestResponse = client.sendSignatureRequest(signatureRequest);

        // validate signature request response
        validateResponse(signatureRequest, signatureRequestResponse, true);

        // get and validate signature request from database
        String signatureRequestId = signatureRequestResponse.getSignatureRequestId();
        SignatureRequest createdSignatureRequest = client.getSignatureRequest(signatureRequestId);

        validateSignatureRequest(signatureRequest, createdSignatureRequest);
    }

    /**
     * The same as {@link #testSendSignatureRequestForFile()} but with file from InputStream.
     */
    @Test
    public void testSendSignatureRequestForInputStream() throws FileNotFoundException {
        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setSendEmails(SEND_EMAILS);

        // add document from input stream and one signer
        URL url = getClass().getResource(TEST_DOCUMENT_LOCATION);
        InputStream inputStream = new FileInputStream(new File(url.getFile()));
        Document document = new Document(inputStream, "TestSendSignatureRequestForInputStream.pdf");
        document.addSigner(new Signer(SIGNER_EMAIL[0]));
        signatureRequest.addDocument(document);

        // execute signature request
        SignatureRequest signatureRequestResponse = client.sendSignatureRequest(signatureRequest);

        // validate signature request response
        validateResponse(signatureRequest, signatureRequestResponse, true);

        // get and validate signature request from database
        String signatureRequestId = signatureRequestResponse.getSignatureRequestId();
        SignatureRequest createdSignatureRequest = client.getSignatureRequest(signatureRequestId);

        validateSignatureRequest(signatureRequest, createdSignatureRequest);

    }


    /**
     * Test to send signature request with fields.
     * Example string for curl:
     * {"documents" : [
     *   {"document_id": "d083448d-ad0e-4742-af19-cfc1117445b1",
     *    "signers": [{"email": "signer_1@example.com",
     *         "fields": [{"page": 0, "rectangle": [100, 100, 300, 200], "type": "SIGNATURE"},
     *                    {"page": 0, "rectangle": [350, 100, 400, 200], *"type": "TEXT"}]}]}]}
     */
    @Test
    public void testSendSignatureRequestWithFields() {

        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setSendEmails(SEND_EMAILS);

        // add document from file and one signer
        URL url = getClass().getResource(TEST_DOCUMENT_LOCATION);
        Document document = new Document(new File(url.getFile()), "TestSendSignatureRequestWithFields.pdf");
        Signer signer = new Signer(SIGNER_EMAIL[0]);
        // add field with API ID to be able find it latter
        Field field1 = new Field(FIELD_PAGE[0][0], FIELD_RECTANGLE[0][0], FieldType.SIGNATURE);
        field1.setApiId(FIELD_API_ID[0][0]);
        signer.addField(field1);
        // add second field to signer
        Field field2 = new Field(FIELD_PAGE[0][1], FIELD_RECTANGLE[0][1], FieldType.TEXT);
        field2.setApiId(FIELD_API_ID[0][1]);
        signer.addField(field2);

        document.addSigner(signer);

        signatureRequest.addDocument(document);

        // execute signature request
        SignatureRequest signatureRequestResponse = client.sendSignatureRequest(signatureRequest);

        // validate signature request response
        validateResponse(signatureRequest, signatureRequestResponse, true);

        // get and validate signature request from database
        SignatureRequest createdSignatureRequest = client.getSignatureRequest(
                signatureRequestResponse.getSignatureRequestId());
        validateSignatureRequest(signatureRequest, createdSignatureRequest);

        // get and validate fields from database
        Document expectedDocument = signatureRequest.getDocuments().get(0);
        DocumentFields documentFields = client.getDocumentFields(expectedDocument.getId());
        validateDocumentFields(expectedDocument, documentFields);
    }
}
