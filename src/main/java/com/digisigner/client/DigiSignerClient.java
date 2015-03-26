package com.digisigner.client;

import com.digisigner.client.data.Document;
import com.digisigner.client.requests.SignatureRequest;
import com.digisigner.client.requests.UploadDocumentRequest;

public class DigiSignerClient {
    private final String apiKey;

    public DigiSignerClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public SignatureRequest sendSignatureRequest(SignatureRequest signatureRequest) {
        return new SignatureRequest();
    }

    public Document uploadDocument(Document document) {
        UploadDocumentRequest request = new UploadDocumentRequest().addDocument(document);
        request.execute(apiKey);
        document.setDocumentId(request.getDocumentId());

        return document;
    }
}
