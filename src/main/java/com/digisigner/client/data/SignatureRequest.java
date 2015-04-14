package com.digisigner.client.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The signature request class holds data to make signature request to sign documents.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SignatureRequest {

    @XmlElement(name = "signature_request_id")
    private String signatureRequestId;

    @XmlElement(name = "is_completed")
    private Boolean completed;

    @XmlElement(name = "documents")
    private final List<Document> documents = new ArrayList<>();

    public void addDocument(Document document) {
        documents.add(document);
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public String getSignatureRequestId() {
        return signatureRequestId;
    }

    public Boolean getCompleted() {
        return completed;
    }
}
