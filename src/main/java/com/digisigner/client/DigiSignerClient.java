package com.digisigner.client;

import java.io.File;

import org.json.JSONObject;

import com.digisigner.client.data.Document;
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.http.Config;
import com.digisigner.client.http.GetRequest;
import com.digisigner.client.http.PostRequest;
import com.digisigner.client.http.Response;

/**
 * Main class for DigiSigner client.
 */
public class DigiSignerClient {
    private final String apiKey;
    private final String serverUrl;

    public DigiSignerClient(String apiKey) {
        this.apiKey = apiKey;
        serverUrl = Config.DEFAULT_SERVER_URL;
    }

    public DigiSignerClient(String serverUrl, String apiKey) {
        this.apiKey = apiKey;
        this.serverUrl = serverUrl;
    }

    /**
     * Sends the signature request to the server.
     *
     * @param signatureRequest filled signature request with required data.
     * @return result with sent signature request ID.
     */
    public SignatureRequest sendSignatureRequest(SignatureRequest signatureRequest) {
        for (Document document : signatureRequest.getDocuments()) {
            if (document.getId() == null) {
                document.setId(callUploadDocument(document));
            }
        }
        return new PostRequest(apiKey).postAsJson(SignatureRequest.class, signatureRequest,
                Config.getSignatureRequestsUrl(serverUrl));
    }

    /**
     * The get signature request to check information about signature such as signature is completed
     * and IDs of signature request and documents.
     *
     * @param signatureRequestId ID of the signature request.
     * @return {@link com.digisigner.client.data.SignatureRequest} with filled IDs and signature request data.
     */
    public SignatureRequest getSignatureRequest(String signatureRequestId) {
        String url = Config.getSignatureRequestsUrl(serverUrl) + "/" + signatureRequestId;
        return new GetRequest(apiKey).getAsJson(SignatureRequest.class, url);
    }

    /**
     * Uploads current document to the server.
     *
     * @param document to upload.
     * @return document with ID.
     */
    public Document uploadDocument(Document document) {
        document.setId(callUploadDocument(document));
        return document;
    }

    /**
     * Upload document and returns ID of document.
     *
     * @param document to upload.
     * @return ID of uploaded document.
     */
    private String callUploadDocument(Document document) {
        Response response = new PostRequest(apiKey).sendDocumentToServer(Config.getDocumentUrl(serverUrl), document);
        return new JSONObject(response.getContent()).getString(Config.PARAM_DOC_ID);
    }

    /**
     * Download the document by ID.
     *
     * @param documentId ID of document.
     * @param fileName   the name of the document file to be saved.
     * @return retrieved document.
     */
    public Document getDocumentById(String documentId, String fileName) {
        File file = new GetRequest(apiKey).getFileResponse(Config.getDocumentUrl(serverUrl), documentId, fileName);

        Document document = new Document(file);
        document.setId(documentId);
        return document;
    }
}
