package com.digisigner.client.requests;

import java.io.File;

import com.digisigner.client.data.Document;
import com.digisigner.client.http.Config;
import com.digisigner.client.http.GetRequest;

/**
 * The request to get a document form server.
 */
public class GetDocumentRequest extends BaseRequest {
    private final String documentId;
    private final String documentName;
    private File file;

    public GetDocumentRequest(String documentId, String documentName) {
        this.documentId = documentId;
        this.documentName = documentName;
    }

    @Override
    public void execute(String apiKey) {
        file = new GetRequest(apiKey, Config.DOCUMENTS_URL).getFileResponse(documentId, documentName);
    }

    public Document getDocument() {
        Document document = new Document(file);
        document.setDocumentId(documentId);
        return document;
    }
}
