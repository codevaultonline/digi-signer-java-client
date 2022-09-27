package com.digisigner.client;

import java.io.File;

import org.junit.Test;

/**
 * Test for the get document attachment request.
 */
public class GetDocumentAttachmentsTest {

    // server URL
    private static final String SERVER_URL = TestsConfigUtil.getServerUrl();

    // test account API key
    private static final String API_KEY = TestsConfigUtil.getApiKey();

    // API client
    private static final DigiSignerClient client = new DigiSignerClient(SERVER_URL, API_KEY);

    // file name for downloaded attachment
    private static final String ATTACHMENT_FILE_NAME = "attachment.txt";

    @Test
    public void testAddContentToDocument() {

        try {
            String documentId = TestsConfigUtil.getAttachmentDocumentId();
            String fieldApiId = TestsConfigUtil.getAttachmentFieldId();

            File file = client.getDocumentAttachment(documentId, fieldApiId, ATTACHMENT_FILE_NAME);
            System.out.println("File attachments location: " + file.getAbsolutePath());
        }
        catch (DigiSignerException e) { // in case http code is wrong
            System.err.println(e.getMessage());
            if (e.getErrors() != null) {
                for (String error : e.getErrors()) {
                    System.err.println(error);
                }
            }
        }
    }
}
