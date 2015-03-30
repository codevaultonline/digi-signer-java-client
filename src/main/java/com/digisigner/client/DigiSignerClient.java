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

    public DigiSignerClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public SignatureRequest sendSignatureRequest(SignatureRequest signatureRequest) {
        for (Document document : signatureRequest.getDocuments()) {
            if (document.getId() == null) {
                document.setId(callUploadDocument(document));
            }
        }
        return new PostRequest(apiKey).postAsJson(SignatureRequest.class, signatureRequest,
                Config.SIGNATURE_REQUESTS_URL);
    }

    public Document uploadDocument(Document document) {
        document.setId(callUploadDocument(document));
        return document;
    }

    /**
     * Upload document and returns ID of document.
     * @param document to upload.
     * @return ID of uploaded document.
     */
    private String callUploadDocument(Document document) {
        Response response = new PostRequest(apiKey).sendDocumentToServer(Config.DOCUMENTS_URL, document);
        return new JSONObject(response.getContent()).getString(Config.PARAM_DOC_ID);
    }

    public Document getDocumentById(String documentId, String fileName) {
        File file = new GetRequest(apiKey).getFileResponse(Config.DOCUMENTS_URL, documentId, fileName);

        Document document = new Document(file);
        document.setId(documentId);
        return document;
    }
}
