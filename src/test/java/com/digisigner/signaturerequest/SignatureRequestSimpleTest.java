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
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.data.Signer;

/**
 * SignatureRequestSimpleTest demonstrates and tests the most simple API usage,
 * one signer, only required attributes are set.
 */
public class SignatureRequestSimpleTest extends SignatureRequestTest {

    private static final String DOCUMENT_ID = "a4f6850b-713d-4809-a3f5-5184495ec12e";
    private static final String TEST_DOCUMENT_LOCATION = "/document.pdf";

    // API client
    private DigiSignerClient client = new DigiSignerClient(SERVER_URL, API_KEY);

    /**
     * Tests send signature request for file.
     * Example string for curl:
     * {"send_emails": false,
     *   "documents" : [
     *     {"document_id": "f1cd390a-70cd-4b9e-9b11-288016e9985f", "signers": [{"email": "signer_1@example.com"}]}]
     * }
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
        validateResponse(signatureRequest, signatureRequestResponse);

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
        validateResponse(signatureRequest, signatureRequestResponse);

        // get and validate signature request from database
        String signatureRequestId = signatureRequestResponse.getSignatureRequestId();
        SignatureRequest createdSignatureRequest = client.getSignatureRequest(signatureRequestId);

        validateSignatureRequest(signatureRequest, createdSignatureRequest);

    }


    public void testSendSignatureRequestWithFields() {

        // send signature request
        /*
        {"documents" : [
          {"document_id": "d083448d-ad0e-4742-af19-cfc1117445b1",
           "signers": [
            {"email": "signer_1@example.com",
             "fields": [
              {"page": 0,
               "rectangle": [100, 100, 300, 200],
               "type": "SIGNATURE"
              },
              {"page": 0,
               "rectangle": [350, 100, 400, 200],
               "type": "TEXT"
              }
             ]
            }
           ]
          }
        ]}
        */

        // build signature request
        SignatureRequest signatureRequest = new SignatureRequest();
//		........................

        // execute signature request
        SignatureRequest signatureRequestResponse = client.sendSignatureRequest(signatureRequest);

        // validate signature request response
        validateResponse(signatureRequest, signatureRequestResponse);

        // get and validate signature request from database
        SignatureRequest createdSignatureRequest = client.getSignatureRequest(signatureRequest.getSignatureRequestId());
        validateSignatureRequest(signatureRequest, createdSignatureRequest);

        // get and validate fields from database
        Document document = signatureRequest.getDocuments().get(0);
        DocumentFields documentFields = client.getDocumentFields(document.getId());
        validateDocumentFields(document, documentFields);
    }
}
