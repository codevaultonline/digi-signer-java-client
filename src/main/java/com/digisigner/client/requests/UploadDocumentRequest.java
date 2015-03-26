package com.digisigner.client.requests;

import com.digisigner.client.data.Document;
import com.digisigner.client.http.Config;
import com.digisigner.client.http.PostRequest;
import com.digisigner.client.http.Response;
import org.json.JSONObject;

/**
 *
 */
public class UploadDocumentRequest extends BaseRequest {

    private Document document;
    private String documentId;

    @Override
    public void execute(String apiKey) {
        Response response = new PostRequest(apiKey).sendDocumentToServer(document);
        documentId = new JSONObject(response.getContent()).getString(Config.PARAM_DOC_ID);
    }

    public UploadDocumentRequest addDocument(Document document) {
        this.document = document;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }
}
