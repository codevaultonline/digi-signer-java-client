package com.digisigner.client;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import com.digisigner.client.data.Document;

/**
 * Tests delete document API call.
 */
public class DeleteDocumentTest {

    private String SERVER_URL = TestsConfigUtil.getServerUrl();
    private static final String API_KEY = TestsConfigUtil.getApiKey();
    private static final String TEST_DOCUMENT_LOCATION = "/document.pdf";

    // API client
    private DigiSignerClient client = new DigiSignerClient(SERVER_URL, API_KEY);

    @Test
    public void testDeleteUploadedDocument() {
        // create document from the resources folder
        URL url = getClass().getResource(TEST_DOCUMENT_LOCATION);
        Document document = new Document(new File(url.getFile()), "testDeleteUploadedDocument.pdf");

        // upload document to the server
        Document uploadedDocument = client.uploadDocument(document);

        // check if document was saved
        assertNotNull("Document failed to upload", uploadedDocument);
        assertNotNull("Document ID is empty", uploadedDocument.getId());

        // delete document
        client.deleteDocument(uploadedDocument.getId());
    }

    @Test
    public void testTryDeleteAlreadyDeletedDocument() {

        // create document from the resources folder
        URL url = getClass().getResource(TEST_DOCUMENT_LOCATION);
        Document document = new Document(new File(url.getFile()), "testTryDeleteAlreadyDeletedDocument.pdf");

        // upload document to the server
        Document uploadedDocument = client.uploadDocument(document);

        // check if document was saved
        assertNotNull("Document failed to upload", uploadedDocument);
        assertNotNull("Document ID is empty", uploadedDocument.getId());

        // delete document
        client.deleteDocument(uploadedDocument.getId());

        // delete document one more time in order to produce exception
        try {
            client.deleteDocument(uploadedDocument.getId());
        } catch (DigiSignerException e) {
            assertNotNull("Error message is empty, but document doesn't exist.", e.getMessage());
        }

    }
}
