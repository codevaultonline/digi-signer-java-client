package com.digisigner.client.http;

/**
 * Settings for the configuration.
 */
public final class Config {

    public static final String DEFAULT_SERVER_URL = "https://api.digisigner.com";

    private static final String VERSION = "/v1";
    private static final String DOCUMENTS_URL = "/documents";
    private static final String SIGNATURE_REQUESTS_URL = "/signature_requests";
    private static final String FIELDS_URL = "/fields";
    private static final String CONTENT_URL = "/content";
    private static final String ATTACHMENT_URL = "/attachment";
    private static final String SLASH = "/";

    public static final String PARAM_DOC_ID = "document_id";

    private Config() {
    }

    public static String getDocumentsUrl(String server) {
        return server + VERSION + DOCUMENTS_URL;
    }

    public static String getDocumentUrl(String server, String documentId) {
        return server + VERSION + DOCUMENTS_URL + SLASH + documentId;
    }

    public static String getFieldsUrl(String server, String documentId) {
        return getDocumentsUrl(server) + SLASH + documentId + FIELDS_URL;
    }

    public static String getContentUrl(String server, String documentId) {
        return getDocumentsUrl(server) + SLASH + documentId + CONTENT_URL;
    }

    public static String getSignatureRequestsUrl(String server) {
        return server + VERSION + SIGNATURE_REQUESTS_URL;
    }

    public static String getDeleteDocumentUrl(String server, String documentId) {
        return getDocumentsUrl(server) + SLASH + documentId;
    }

    public static String getDocumentAttachmentUrl(String server, String documentId, String fieldApiId) {
        return getDocumentsUrl(server) + SLASH + documentId + FIELDS_URL + SLASH + fieldApiId + ATTACHMENT_URL;
    }
}
