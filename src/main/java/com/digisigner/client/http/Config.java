package com.digisigner.client.http;

/**
 * Settings for the configuration.
 */
public class Config {
    public static final String SERVER = "http://localhost:8080/online/api/";
    public static final String VERSION = "/v1";
    public static final String SERVER_URL = SERVER + VERSION;
    public static final String DOCUMENTS_PATH = "/documents";
    public static final String SIGNATURE_REQUESTS_PATH = "/signature_requests";

    public static final String PARAM_DOC_ID = "document_id";

}
