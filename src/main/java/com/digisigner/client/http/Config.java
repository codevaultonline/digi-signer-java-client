package com.digisigner.client.http;

/**
 * Settings for the configuration.
 */
public final class Config {

    public static final String SERVER = "http://localhost:8080/online/api";
    public static final String VERSION = "/v1";
    public static final String SERVER_URL = SERVER + VERSION;
    public static final String DOCUMENTS_URL = SERVER_URL + "/documents";
    public static final String SIGNATURE_REQUESTS_URL = SERVER_URL + "/signature_requests";

    public static final String PARAM_DOC_ID = "document_id";

    private Config() {
    }

}
