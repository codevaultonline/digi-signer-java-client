package com.digisigner.client;

import com.digisigner.client.data.Document;
import com.digisigner.client.http.Config;
import com.digisigner.client.http.GetRequest;
import com.digisigner.client.http.PostRequest;
import com.digisigner.client.http.Response;
import com.digisigner.client.data.SignatureRequest;
import org.json.JSONObject;

import java.io.File;

/**
 * Main class for DigiSigner client.
 */
public class DigiSignerClient {
    private final String apiKey;

    public DigiSignerClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public SignatureRequest sendSignatureRequest(SignatureRequest signatureRequest) {
        return new PostRequest(apiKey).postAsJson(SignatureRequest.class, signatureRequest,
                Config.SIGNATURE_REQUESTS_URL);
    }

    public Document uploadDocument(Document document) {
        Response response = new PostRequest(apiKey).sendDocumentToServer(Config.DOCUMENTS_URL, document);
        String documentId = new JSONObject(response.getContent()).getString(Config.PARAM_DOC_ID);
        document.setDocumentId(documentId);

        return document;
    }

    public Document getDocumentById(String documentId, String fileName) {
        File file = new GetRequest(apiKey).getFileResponse(Config.DOCUMENTS_URL, documentId, fileName);

        Document document = new Document(file);
        document.setDocumentId(documentId);
        return document;
    }
}
