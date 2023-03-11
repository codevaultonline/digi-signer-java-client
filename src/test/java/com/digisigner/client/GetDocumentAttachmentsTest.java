package com.digisigner.client;

import java.io.File;

import org.junit.Test;

/**
 * Test for the get document attachment request.
 */
public class GetDocumentAttachmentsTest {
    private static final String DESTINATION_FILE_NAME = "destinationFileName.pdf";

    private static String SERVER_URL = TestsConfigUtil.getServerUrl();

    /**
     * The api key for test.
     * The value can be found in the DigiSigner account (Settings dialog).
     */
    private static final String API_KEY = TestsConfigUtil.getApiKey();

    private static final String MESSAGE = "Message: ";

    // API client
    private final DigiSignerClient client = new DigiSignerClient(SERVER_URL, API_KEY);

    @Test
    public void testAddContentToDocument() {

        try {
            String documentId = TestsConfigUtil.getAttachmentDocumentId();
            String fieldApiId = TestsConfigUtil.getAttachmentFieldId();

            File file = client.getDocumentAttachment(documentId, fieldApiId, DESTINATION_FILE_NAME);
            System.out.println("File attachments location: " + file.getAbsolutePath());

        } catch (DigiSignerException e) { // in case http code is wrong
            System.err.println(MESSAGE + e.getMessage());
            if (e.getErrors() != null) {
                for (String error : e.getErrors()) {
                    System.err.println(error);
                }
            }
        }
    }
}
