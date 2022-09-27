package com.digisigner.signaturerequest;

import org.junit.Test;

import com.digisigner.client.DigiSignerClient;
import com.digisigner.client.data.Document;
import com.digisigner.client.data.DocumentFields;
import com.digisigner.client.data.ExistingField;
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.data.Signer;

/**
 * SignatureRequestForTemplateSimpleTest demonstrates and tests the most simple API usage for template,
 * one signer, only required attributes are set.
 */
public class SignatureRequestForTemplateSimpleTest extends SignatureRequestTest {

    // API client
    private static final DigiSignerClient client = new DigiSignerClient(SERVER_URL, API_KEY);

    /**
     * Simple test to send signature request for template.
     * Curl example:
     * {"documents" : [{"document_id": "6586b79c-60a9-4986-a96d-2b8841cfb567",
     * "signers": [{"email": "signer_1@example.com"}]}]}
     */
    @Test
    public void testSendSignatureRequest() {

        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setSendEmails(SEND_EMAILS);

        // build template
        Document template = new Document(TEMPLATE_ID);

        // add signer to template
        template.addSigner(new Signer(SIGNER_EMAIL[0]));

        // add template to signature request
        signatureRequest.addDocument(template);

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
     * Test signature request for template with fields.
     * Curl example:
     * {"documents" : [{"document_id": "6586b79c-60a9-4986-a96d-2b8841cfb567",
     * "signers": [{"email": "signer_1@example.com", "role": "Signer 1",
     * "existing_fields": [
     * {"api_id": "d9fbf81b-cfa1-47cd-bc3e-3980131af733", "content": "Sample content 1"},
     * {"api_id": "00b25bcc-7909-4174-b18c-04ae2fb01775", "content": "Sample content 2"}
     * ]}]}]}
     */
    @Test
    public void testSendSignatureRequestWithExistingFields() {

        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setSendEmails(SEND_EMAILS);

        // build template
        Document document = new Document(TEMPLATE_ID);

        // build signer
        Signer signer = new Signer(SIGNER_EMAIL[0]);
        signer.setRole(SIGNER_ROLE[0]);

        // add fields to signer
        ExistingField field1 = new ExistingField(EXISTING_FIELD_API_ID[0][0]);
        field1.setContent(FIELD_CONTENT[0][0]);
        field1.setRequired(FIELD_REQUIRED[0][0]);
        signer.addExistingField(field1);

        ExistingField field2 = new ExistingField(EXISTING_FIELD_API_ID[0][1]);
        field2.setContent(FIELD_CONTENT[0][1]);
        field2.setRequired(FIELD_REQUIRED[0][1]);
        signer.addExistingField(field2);

        // add signer to document
        document.addSigner(signer);

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

