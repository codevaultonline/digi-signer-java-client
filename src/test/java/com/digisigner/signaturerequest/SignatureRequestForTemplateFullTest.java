package com.digisigner.signaturerequest;

import org.junit.Test;

import com.digisigner.client.DigiSignerClient;
import com.digisigner.client.data.Document;
import com.digisigner.client.data.DocumentFields;
import com.digisigner.client.data.ExistingField;
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.data.Signer;

/**
 * FullTest for template demonstrates and tests full API usage, two signers, all possible attributes are set.
 */
public class SignatureRequestForTemplateFullTest extends SignatureRequestTest {

    // API client
    private static final DigiSignerClient client = new DigiSignerClient(SERVER_URL, API_KEY);

    /**
     * Test sending signature request for template.
     * Curl example:
     * {"documents" : [
     * {"document_id": "6586b79c-60a9-4986-a96d-2b8841cfb567",
     * "title": "Sample title", "subject": "Sample subject", "message": "Sample message",
     * "signers": [{"email": "signer_1@example.com"},{"email": "signer_2@example.com"}]}]}
     */
    @Test
    public void testSendSignatureRequest() {

        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setSendEmails(SEND_EMAILS);

        // build document
        Document document = new Document(TEMPLATE_ID);
        document.setTitle(TITLE);
        document.setSubject(SUBJECT);
        document.setMessage(MESSAGE);

        // build signer 1
        Signer signer1 = new Signer(SIGNER_EMAIL[0]);
        signer1.setOrder(SIGNER_ORDER[0]);

        // build signer 2
        Signer signer2 = new Signer(SIGNER_EMAIL[1]);
        signer2.setOrder(SIGNER_ORDER[1]);

        // add signers to document
        document.addSigner(signer1);
        document.addSigner(signer2);

        // add document to signature request
        signatureRequest.addDocument(document);

        // execute signature request
        SignatureRequest signatureRequestResponse = client.sendSignatureRequest(signatureRequest);

        // validate signature request response
        validateResponse(signatureRequest, signatureRequestResponse, false);

        // get and validate signature request from database
        String signatureRequestId = signatureRequestResponse.getSignatureRequestId();
        SignatureRequest createdSignatureRequest = client.getSignatureRequest(signatureRequestId);
        validateSignatureRequest(signatureRequest, createdSignatureRequest, false);
    }

    /**
     * Test sending signature request with fields for template.
     * Curl example:
     * {"documents" : [
     * {"document_id": "79fbdbc7-dbac-424d-8e2e-507ea4ebb53f",
     * "title": "Sample title", "subject": "Sample subject", "message": "Sample message",
     * "signers": [
     * {"email": "signer_1@example.com", "role": "Employee",
     * "existing_fields": [
     * {"api_id": "d9fbf81b-cfa1-47cd-bc3e-3980131af733", "content": "Sample content 1", "label": "Please sign",
     * "required": true, "read_only": false },
     * {"api_id": "00b25bcc-7909-4174-b18c-04ae2fb01775", "content": "James Williams", "label": "Your name",
     * "required": true, "read_only": false }
     * ]},
     * {"email": "signer_2@example.com", "role": "Manager", "existing_fields": [
     * {"api_id": "b96211e4-08bc-4d6d-8498-30a991ff39f3", "content": "Mary Brown",
     * "label": "Please sign", "required": true, "read_only": false},
     * {"api_id": "5ac9c8c5-4f4d-4a1b-b2e1-4eb07f9f579f", "content": "Mary Brown", "label": "Your name",
     * "required": false, "read_only": false}]}]}]}
     */
    @Test
    public void testSendSignatureRequestWithExistingFields() {

        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setSendEmails(SEND_EMAILS);

        // build document
        Document document = new Document(TEMPLATE_ID);
        document.setTitle(TITLE);
        document.setSubject(SUBJECT);
        document.setMessage(MESSAGE);

        // build signer 1
        Signer signer1 = new Signer(SIGNER_EMAIL[0]);
        signer1.setRole(SIGNER_ROLE[0]);
        signer1.setOrder(SIGNER_ORDER[0]);
        
        // add fields for signer 1
        ExistingField field1 = new ExistingField(EXISTING_FIELD_API_ID[0][0]);
        field1.setContent(FIELD_CONTENT[0][0]);
        field1.setLabel(FIELD_LABEL[0][0]);
        field1.setRequired(FIELD_REQUIRED[0][0]);
        field1.setReadOnly(FIELD_READ_ONLY[0][0]);
        signer1.addExistingField(field1);

        ExistingField field2 = new ExistingField(EXISTING_FIELD_API_ID[0][1]);
        field2.setContent(FIELD_CONTENT[0][1]);
        field2.setLabel(FIELD_LABEL[0][1]);
        field2.setRequired(FIELD_REQUIRED[0][1]);
        field2.setReadOnly(FIELD_READ_ONLY[0][1]);
        signer1.addExistingField(field2);

        // build signer 2
        Signer signer2 = new Signer(SIGNER_EMAIL[1]);
        signer2.setRole(SIGNER_ROLE[1]);
        signer2.setOrder(SIGNER_ORDER[1]);

        // add field for signer 2
        ExistingField field3 = new ExistingField(EXISTING_FIELD_API_ID[1][0]);
        field3.setContent(FIELD_CONTENT[1][0]);
        field3.setLabel(FIELD_LABEL[1][0]);
        field3.setRequired(FIELD_REQUIRED[1][0]);
        field3.setReadOnly(FIELD_READ_ONLY[1][0]);
        signer2.addExistingField(field3);

        ExistingField field4 = new ExistingField(EXISTING_FIELD_API_ID[1][1]);
        field4.setContent(FIELD_CONTENT[1][1]);
        field4.setLabel(FIELD_LABEL[1][1]);
        field4.setRequired(FIELD_REQUIRED[1][1]);
        field4.setReadOnly(FIELD_READ_ONLY[1][1]);
        signer2.addExistingField(field4);

        // add signers to document
        document.addSigner(signer1);
        document.addSigner(signer2);

        // add document to signature request
        signatureRequest.addDocument(document);

        // execute signature request
        SignatureRequest signatureRequestResponse = client.sendSignatureRequest(signatureRequest);

        // validate signature request response
        validateResponse(signatureRequest, signatureRequestResponse, false);

        // get and validate signature request from database
        SignatureRequest createdSignatureRequest = client.getSignatureRequest(
                signatureRequestResponse.getSignatureRequestId());
        validateSignatureRequest(signatureRequest, createdSignatureRequest, false);

        // get and validate fields from database
        Document expectedDocument = signatureRequest.getDocuments().get(0);
        DocumentFields documentFields = client.getDocumentFields(createdSignatureRequest.getDocuments().get(0).getId());
        validateDocumentFields(expectedDocument, documentFields);
    }
}