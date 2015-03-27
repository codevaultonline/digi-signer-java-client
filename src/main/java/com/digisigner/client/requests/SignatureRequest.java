package com.digisigner.client.requests;

import java.util.ArrayList;
import java.util.List;

import com.digisigner.client.data.Document;

/**
 * The signature request class holds data to make signature request to sign documents.
 */
public class SignatureRequest extends BaseRequest {

    private List<Document> documents = new ArrayList<>();

    @Override
    public void execute(String apiKey) {

    }

    public void addDocument(Document document) {
        documents.add(document);
    }

    public String getId() {
        return null;
    }

}
