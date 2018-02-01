package com.digisigner.signaturerequest;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.digisigner.client.DigiSignerClient;
import com.digisigner.client.data.Document;
import com.digisigner.client.data.DocumentFields;
import com.digisigner.client.data.Field;
import com.digisigner.client.data.FieldType;
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.data.Signer;

/**
 * FullTest demonstrates and tests full API usage, two signers, all possible attributes are set.
 */
public class SignatureRequestFullTest extends SignatureRequestTest {

    // API client
    private DigiSignerClient client = new DigiSignerClient(SERVER_URL, API_KEY);

    /**
     * Tests send signature request.
     * Curl example:
     * {"documents" : [
     * {"document_id": "06c4d320-d6c5-492b-b343-8482338ef9d0",
     * "title": "Sample title",
     * "subject": "Sample subject",
     * "message": "Sample message",
     * "signers": [*{"email": "signer_1@example.com"},{"email": "signer_2@example.com"}]}]}
     */
    @Test
    public void testSendSignatureRequest() {

        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setSendEmails(SEND_EMAILS);

        // add document with possible attributes
        URL url = getClass().getResource(TEST_DOCUMENT_LOCATION);
        Document document = new Document(new File(url.getFile()), "TestSendSignatureRequest.pdf");
        document.setTitle(TITLE);
        document.setSubject(SUBJECT);
        document.setMessage(MESSAGE);
        
        Signer signer1 = new Signer(SIGNER_EMAIL[0]);
        signer1.setOrder(SIGNER_ORDER[0]);
        
        Signer signer2 = new Signer(SIGNER_EMAIL[1]);
        signer2.setOrder(SIGNER_ORDER[1]);
		
        document.addSigner(signer1);
        document.addSigner(signer2);
        signatureRequest.addDocument(document);

        // execute signature request
        SignatureRequest signatureRequestResponse = client.sendSignatureRequest(signatureRequest);

        // validate signature request response
        validateResponse(signatureRequest, signatureRequestResponse, true);

        // get and validate signature request from database
        String signatureRequestId = signatureRequestResponse.getSignatureRequestId();
        SignatureRequest createdSignatureRequest = client.getSignatureRequest(signatureRequestId);

        assertTrue("Wrong number of signers", createdSignatureRequest.getDocuments().get(0).getSigners().size() == 2);
        validateSignatureRequest(signatureRequest, createdSignatureRequest, true);
    }

    /**
     * Tests send signature request with fields.
     * Curl example:
     * {"documents" : [
     * {"document_id": "c8860903-2278-45a1-8daf-f8e7dd73d4da",
     * "title": "Sample title",
     * "subject": "Sample subject",
     * "message": "Sample message",
     * "signers": [
     * {"email": "signer_1@example.com", "role": "Signer 1",
     * "fields": [
     * {"page": 0, "rectangle": [100, 100, 300, 200], "type": "SIGNATURE", "label": "Sample label 1",
     * "content": "Sample content 1", "api_id": "Sample api id 1", "required": true, "read_only": false },
     * {"page": 0, "rectangle": [400, 100, 450, 130], "type": "TEXT", "label": "Sample label 2",
     * "content": "Sample content 2", "api_id": "Sample api id 2", "required": true, "read_only": false}]
     * },
     * {"email": "signer_2@example.com", "role": "Signer 2",
     * "fields": [
     * {"page": 0, "rectangle": [100, 300, 300, 400], "type": "SIGNATURE", "label": "Sample label 3",
     * "content": "Sample content 3", "api_id": "Sample api id 3", "required": false, "read_only": false
     * },
     * {"page": 0, "rectangle": [400, 300, 450, 330], "type": "TEXT", "label": "Sample label 4",
     * "content": "Sample content 4", "api_id": "Sample api id 4", "required": true, "read_only": true}]
     * }]}]}
     */
    @Test
    public void testSendSignatureRequestWithFields() {
        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setSendEmails(SEND_EMAILS);

        // add document with possible attributes
        URL url = getClass().getResource(TEST_DOCUMENT_LOCATION);
        Document document = new Document(new File(url.getFile()), "TestSendSignatureRequestWithFields.pdf");
        document.setTitle(TITLE);
        document.setSubject(SUBJECT);
        document.setMessage(MESSAGE);

        // add first signer
        Signer signer1 = new Signer(SIGNER_EMAIL[0]);
        signer1.setRole(SIGNER_ROLE[0]);
        signer1.setOrder(SIGNER_ORDER[0]);
        // add fields for first signer
        Field field1 = new Field(FIELD_PAGE[0][0], FIELD_RECTANGLE[0][0], FieldType.SIGNATURE);
        field1.setApiId(FIELD_API_ID[0][0]);
        field1.setLabel(FIELD_LABEL[0][0]);
        field1.setContent(FIELD_CONTENT[0][0]);
        field1.setRequired(FIELD_REQUIRED[0][0]);
        field1.setReadOnly(FIELD_READ_ONLY[0][0]);
        signer1.addField(field1);

        Field field2 = new Field(FIELD_PAGE[0][1], FIELD_RECTANGLE[0][1], FieldType.TEXT);
        field2.setApiId(FIELD_API_ID[0][1]);
        field2.setLabel(FIELD_LABEL[0][1]);
        field2.setContent(FIELD_CONTENT[0][1]);
        field2.setRequired(FIELD_REQUIRED[0][1]);
        field2.setReadOnly(FIELD_READ_ONLY[0][1]);
        signer1.addField(field2);

        // add second signer
        Signer signer2 = new Signer(SIGNER_EMAIL[1]);
        signer2.setRole(SIGNER_ROLE[1]);
        signer2.setOrder(SIGNER_ORDER[1]);
        // add fields for second signer
        Field field3 = new Field(FIELD_PAGE[1][0], FIELD_RECTANGLE[1][0], FieldType.SIGNATURE);
        field3.setApiId(FIELD_API_ID[1][0]);
        field3.setLabel(FIELD_LABEL[1][0]);
        field3.setContent(FIELD_CONTENT[1][0]);
        field3.setRequired(FIELD_REQUIRED[1][0]);
        field3.setReadOnly(FIELD_READ_ONLY[1][0]);
        signer2.addField(field3);

        Field field4 = new Field(FIELD_PAGE[1][1], FIELD_RECTANGLE[1][1], FieldType.TEXT);
        field4.setApiId(FIELD_API_ID[1][1]);
        field4.setLabel(FIELD_LABEL[1][1]);
        field4.setContent(FIELD_CONTENT[1][1]);
        field4.setRequired(FIELD_REQUIRED[1][1]);
        field4.setReadOnly(FIELD_READ_ONLY[1][1]);
        signer2.addField(field4);

        document.addSigner(signer1);
        document.addSigner(signer2);
        signatureRequest.addDocument(document);

        // execute signature request
        SignatureRequest signatureRequestResponse = client.sendSignatureRequest(signatureRequest);

        // validate signature request response
        validateResponse(signatureRequest, signatureRequestResponse, true);

        // get and validate signature request from database
        String signatureRequestId = signatureRequestResponse.getSignatureRequestId();
        SignatureRequest createdSignatureRequest = client.getSignatureRequest(signatureRequestId);

        assertTrue("Wrong number of signers", createdSignatureRequest.getDocuments().get(0).getSigners().size() == 2);
        validateSignatureRequest(signatureRequest, createdSignatureRequest, true);

        // get and validate fields from database
        Document expectedDocument = signatureRequest.getDocuments().get(0);
        DocumentFields documentFields = client.getDocumentFields(expectedDocument.getId());
        validateDocumentFields(expectedDocument, documentFields);
    }
}