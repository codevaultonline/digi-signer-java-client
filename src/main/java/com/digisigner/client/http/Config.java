package com.digisigner.client.http;

/**
 * Settings for the configuration.
 */
public final class Config {

    public static final String DEFAULT_SERVER_URL = "https://digisigner.com/online/api";

    private static final String VERSION = "/v1";
    private static final String DOCUMENTS_URL = "/documents";
    private static final String SIGNATURE_REQUESTS_URL = "/signature_requests";
    private static final String FIELDS_URL = "/fields";
    private static final String CONTENT_URL = "/content";

    public static final String PARAM_DOC_ID = "document_id";

    private Config() {
    }

    public static String getDocumentUrl(String server) {
        return server + VERSION + DOCUMENTS_URL;
    }

    public static String getFieldsUrl(String server, String documentId) {
        return getDocumentUrl(server) + "/" + documentId + FIELDS_URL;
    }

    public static String getContentUrl(String server, String documentId) {
        return getDocumentUrl(server) + "/" + documentId + CONTENT_URL;
    }

    public static String getSignatureRequestsUrl(String server) {
        return server + VERSION + SIGNATURE_REQUESTS_URL;
    }
}
