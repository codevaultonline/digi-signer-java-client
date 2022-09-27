package com.digisigner.signaturerequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.digisigner.client.TestsConfigUtil;
import com.digisigner.client.data.Document;
import com.digisigner.client.data.DocumentField;
import com.digisigner.client.data.DocumentFields;
import com.digisigner.client.data.ExistingField;
import com.digisigner.client.data.Field;
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.data.Signer;

/**
 * Base for the SignatureRequest tests.
 */
public class SignatureRequestTest {

    // server URL
    protected static final String SERVER_URL = TestsConfigUtil.getServerUrl();

    // test account API key
    protected static final String API_KEY = TestsConfigUtil.getApiKey();

    // should emails be sent during test?
    protected static final boolean SEND_EMAILS = false;  // by default, we don't send emails

    // path to document that we upload to test account
    protected static final String DOCUMENT = "/document.pdf";

    // template ID in test account
    protected static final String TEMPLATE_ID = TestsConfigUtil.getTemplateId();

    // title that document get when it gets uploaded to test account
    protected static final String TITLE = "Sample title";

    // email subject and message (only relevant if we send emails)
    protected static final String SUBJECT = "Sample subject";
    protected static final String MESSAGE = "Sample message";

    // signers
    protected static final String[] SIGNER_EMAIL = new String[]{"signer_1@example.com", "signer_2@example.com"};
    protected static final String[] SIGNER_ROLE = new String[]{"Signer 1", "Signer 2"};
    protected static final Integer[] SIGNER_ORDER = new Integer[]{1, 2};

    // fields that are added to document
    protected static final int[][] FIELD_PAGE = new int[][]{{0, 0}, {0, 0}};
    protected static final int[][][] FIELD_RECTANGLE = new int[][][]{
            {{100, 100, 300, 200}, {400, 100, 450, 130}},
            {{100, 300, 300, 400}, {400, 300, 450, 330}}};
    protected static final String[][] FIELD_CONTENT = new String[][]{
            {"Sample content 1", "Sample content 2"},
            {"Sample content 3", "Sample content 4"}};
    protected static final String[][] FIELD_LABEL = new String[][]{
            {"Sample label 1", "Sample label 2"},
            {"Sample label 3", "Sample label 4"}};
    protected static final String[][] FIELD_API_ID = new String[][]{
            {"Sample API ID 1", "Sample API ID 2"},
            {"Sample API ID 3", "Sample API ID 4"}};
    protected static final boolean[][] FIELD_REQUIRED = new boolean[][]{{true, true}, {false, true}};
    protected static final boolean[][] FIELD_READ_ONLY = new boolean[][]{{false, false}, {false, true}};

    // fields that exist in test template
    protected static final String[][] EXISTING_FIELD_API_ID = new String[][]{
            {TestsConfigUtil.getExistingField(0), TestsConfigUtil.getExistingField(1)},
            {TestsConfigUtil.getExistingField(2), TestsConfigUtil.getExistingField(3)}};


	/* =================  METHODS USED IN ALL SIGNATURE REQUEST TESTS ========================= */

    /**
     * Validate response of signature request.
     *
     * @param expected   signature request.
     * @param actual     signature request.
     * @param isDocument if signature request is for template or is a bundle - {@code false}.
     */
    protected void validateResponse(SignatureRequest expected, SignatureRequest actual, boolean isDocument) {
        for (int i = 0; i < actual.getDocuments().size(); i++) {
            Document document = actual.getDocuments().get(i);
            for (int s = 0; s < document.getSigners().size(); s++) {
                String signDocumentUrl = document.getSigners().get(s).getSignDocumentUrl();
                assertNotNull("The sign document URL cannot be null.", signDocumentUrl);
                // validate signDocumentUrl
                assertTrue("The sign document URL doesn't have required parameters.",
                        signDocumentUrl.matches("(?=.*documentId=)(?=.*invitationId=).*$"));
                // template has different ID for expected SignatureRequest.
                if (isDocument) {
                    String expectedDocumentId = expected.getDocuments().get(i).getId();
                    assertTrue("The sign document URL doesn't have correct document ID.",
                            signDocumentUrl.contains(expectedDocumentId));
                }
            }
        }
    }

    /**
     * Validate signature request.
     *
     * @param expected   signature request.
     * @param actual     signature request.
     * @param isDocument if signature request is for template - {@code false}.
     */
    protected void validateSignatureRequest(SignatureRequest expected, SignatureRequest actual, boolean isDocument) {

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
            Document actualDocument = actual.getDocuments().get(i);
            // template has different ID for expected SignatureRequest.
            if (isDocument) {
                assertEquals("DocumentId: not equals.", expectedDocument.getId(), actualDocument.getId());
            }
            // check document title; if not set - generated
            if (expectedDocument.getTitle() == null) {
                assertNotNull("Default title was not set.", actualDocument.getTitle());
            } else {
                assertEquals("Document Title: not equals.", expectedDocument.getTitle(), actualDocument.getTitle());
            }

            // check document subject and message; if not set - taken by default
            if (expectedDocument.getSubject() == null) {
                assertNotNull("Default subject was not set.", actualDocument.getSubject());
            } else {
                assertEquals("Document Subject: not equals.", expectedDocument.getSubject(), actualDocument.getSubject());
            }

            if (expectedDocument.getMessage() == null) {
                assertNotNull("Default message was not set.", actualDocument.getSubject());
            } else {
                assertEquals("Document Message: not equals.", expectedDocument.getMessage(), actualDocument.getMessage());
            }

            // for each document iterate over signers and assert all their attributes are the same
            for (int s = 0; s < expectedDocument.getSigners().size(); s++) {
                Signer expectedSigner = expectedDocument.getSigners().get(s);
                Signer actualSigner = getSignerByEmail(actual, expectedSigner.getEmail());

                assertEquals("AccessCode: not equals.", expectedSigner.getAccessCode(), actualSigner.getAccessCode());
                assertEquals("Email: not equals.", expectedSigner.getEmail(), actualSigner.getEmail());
                assertEquals("Signer order: not equals.", expectedSigner.getOrder(), actualSigner.getOrder());
                // validate if role defined for signer
                if (expectedSigner.getRole() != null) {
                    assertEquals("Role: not equals.", expectedSigner.getRole(), actualSigner.getRole());
                }
                // validate signDocumentUrl
                assertNotNull("The sign document URL cannot be null!", actualSigner.getSignDocumentUrl());
                assertTrue("The sign document URL doesn't have required parameters.",
                        actualSigner.getSignDocumentUrl().matches("(?=.*documentId=)(?=.*invitationId=).*$"));
                assertEquals("SignatureCompleted: not equals.", getBooleanValue(expectedSigner.getSignatureCompleted()), actualSigner.getSignatureCompleted());
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
                assertEquals("GroupId: not equals.", field.getGroupId(), documentField.getGroupId());
                assertEquals("ReadOnly: not equals.", getBooleanValue(field.getReadOnly()), documentField.isReadOnly());
                assertEquals("Content: not equals.", field.getContent(), documentField.getContent());
            }

            for (ExistingField field : signer.getExistingFields()) {
                DocumentField documentField = findDocumentField(field.getApiId(), documentFields);
                assertNotNull("Document field wasn't found.", documentField);
                // and all their attributes are equal
                assertEquals("Label: not equals.", field.getLabel(), documentField.getLabel());
                assertEquals("Required: not equals.", getBooleanValue(field.getRequired()), documentField.isRequired());
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

    private Signer getSignerByEmail(SignatureRequest signatureRequest, String email) {
        for (Document document : signatureRequest.getDocuments()) {
            for (Signer signer : document.getSigners()) {
                if (signer.getEmail().equals(email)) {
                    return signer;
                }
            }
        }
        return null;
    }
}

