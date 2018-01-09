package com.digisigner.signaturerequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.digisigner.client.data.Document;
import com.digisigner.client.data.DocumentField;
import com.digisigner.client.data.DocumentFields;
import com.digisigner.client.data.Field;
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.data.Signer;

/**
 * Base for the SignatureRequest tests.
 */
public class SignatureRequestTest {

    // constants
//    String SERVER_URL = "http://localhost:8080/digisigner/api";
    String SERVER_URL = "http://localhost:8080/online/api";
    static final String API_KEY = "04a5fa9d-1a8c-4905-8366-26488da6ccc3";

    static final String TEST_DOCUMENT_LOCATION = "/document.pdf";
    boolean SEND_EMAILS = false;  // by default we don't send emails

    // document values
    String SUBJECT = "Sample subject";
    String MESSAGE = "Sample message";
    String TITLE = "Sample title";
    String TEMPLATE_ID = "....";

    // signer values
    String[] SIGNER_EMAIL = new String[]{"signer_1@example.com", "signer_2@example.com"};
    private String[] SIGNER_ROLE = new String[]{"Signer 1", "Signer 2"};

    // field values
    int[][] FIELD_PAGE = new int[][]{{0, 0}, {0, 0}};

    int[][][] FIELD_RECTANGLE = new int[][][]{
            // fields for first signer
            {{100, 100, 300, 200}, {400, 100, 450, 130}},
            // fields for second signer
            {{100, 300, 300, 400}, {400, 300, 450, 330}}};

    String[][] FIELD_API_ID = new String[][]{{"Sample API ID 1", "Sample API ID 2"},
            {"Sample API ID 3", "Sample API ID 4"}};


    String[][] FIELD_CONTENT = new String[][]{{"Sample content 1", "Sample content 2"},
            {"Sample content 3", "Sample content 4"}};
//    String[][] FIELD_LABEL =....
//    String[][] FIELD_API_ID =...
//    boolean[][] FIELD_REQUIRED =...
//    boolean[][] FIELD_READ_ONLY =...
//    String[][] EXISTINGS_FIELD_API_ID =...


	/* =================  METHODS USED IN ALL SIGNATURE REQUEST TESTS ========================= */

    protected void validateResponse(SignatureRequest expected, SignatureRequest actual) {
        for (int i = 0; i < actual.getDocuments().size(); i++) {
            Document document = actual.getDocuments().get(i);
            for (int s = 0; s < document.getSigners().size(); s++) {
                String signDocumentUrl = document.getSigners().get(s).getSignDocumentUrl();
                assertNotNull("The sign document URL cannot be null!", signDocumentUrl);
                // validate signDocumentUrl
                String expectedDocumentId = expected.getDocuments().get(i).getId();
                assertTrue("The sign document URL doesn't have correct document ID.",
                        signDocumentUrl.contains(expectedDocumentId));
            }
        }
    }

    protected void validateSignatureRequest(SignatureRequest expected, SignatureRequest actual) {

        // assert all high level attributes are the same: "send_emails", "embedded" etc.
        assertEquals("SendEmails: not equals.", expected.getSendEmails(), actual.getSendEmails());
        assertEquals("RedirectForSigningToUrl: not equals.", expected.getRedirectForSigningToUrl(), actual.getRedirectForSigningToUrl());
        assertEquals("RedirectAfterSigningToUrl: not equals.", expected.getRedirectAfterSigningToUrl(), actual.getRedirectAfterSigningToUrl());
        assertEquals("Embedded: not equals.", getBooleanValue(expected.getEmbedded()), actual.getEmbedded());
        assertEquals("UseTextTags: not equals.", expected.isUseTextTags(), actual.isUseTextTags());
        assertEquals("HideTextTags: not equals.", expected.isHideTextTags(), actual.isHideTextTags());
        assertEquals("Completed: not equals.", getBooleanValue(expected.getCompleted()), actual.getCompleted());

        // iterate over documents and assert all their attributes are the same
        for (int i = 0; i < expected.getDocuments().size(); i++) {
            Document expectedDocument = expected.getDocuments().get(i);
            Document actualDocument = expected.getDocuments().get(i);
            assertEquals("DocumentId: not equals.", expectedDocument.getId(), actualDocument.getId());
            assertEquals("Document Title: not equals.", expectedDocument.getTitle(), actualDocument.getTitle());
            assertEquals("Document FileName: not equals.", expectedDocument.getFileName(), actualDocument.getFileName());
            assertEquals("Document Subject: not equals.", expectedDocument.getSubject(), actualDocument.getSubject());
            assertEquals("Document Message: not equals.", expectedDocument.getMessage(), actualDocument.getMessage());

            // for each document iterate over signers and assert all their attributes are the same
            for (int s = 0; s < expectedDocument.getSigners().size(); s++) {
                Signer expectedSigner = expectedDocument.getSigners().get(s);
                Signer actualSigner = expectedDocument.getSigners().get(s);

                assertEquals("AccessCode: not equals.", expectedSigner.getAccessCode(), actualSigner.getAccessCode());
                assertEquals("Email: not equals.", expectedSigner.getEmail(), actualSigner.getEmail());
                assertEquals("Role: not equals.", expectedSigner.getRole(), actualSigner.getRole());
                assertEquals("SignDocumentUrl: not equals.", expectedSigner.getSignDocumentUrl(), actualSigner.getSignDocumentUrl());
                assertEquals("SignatureCompleted: not equals.", expectedSigner.getSignatureCompleted(), actualSigner.getSignatureCompleted());
            }
        }
    }

    protected void validateDocumentFields(Document document, DocumentFields documentFields) {


        for (Signer signer : document.getSigners()) {
            for (Field field : signer.getFields()) {
                // assert that all fields from all signers in document (document.getSigners()) can be found
                // in documentFields.getDocumentFields()
                DocumentField documentField = findDocumentField(field.getApiId(), documentFields);
                assertNotNull("Document field wasn't found.", documentField);
                // and all their attributes are equal
                assertEquals("Page: not equals.", field.getPage(), documentField.getPage());
                assertEquals("Type: not equals.", field.getType(), documentField.getType());
                assertEquals("Label: not equals.", field.getLabel(), documentField.getLabel());
                assertEquals("Required: not equals.", field.isRequired(), documentField.isRequired());
                assertEquals("Name: not equals.", field.getName(), documentField.getName());
                assertEquals("ReadOnly: not equals.", getBooleanValue(field.getReadOnly()), documentField.isReadOnly());
                assertEquals("Content: not equals.", field.getContent(), documentField.getContent());

            }
        }
    }

    private boolean getBooleanValue(Boolean value) {
        return value != null && value;
    }

    private DocumentField findDocumentField(String apiId, DocumentFields documentFields) {
        for (DocumentField documentField : documentFields.getDocumentFields()) {
            if (documentField.getApiId().equals(apiId)) {
                return documentField;
            }
        }
        return null;
    }
}

