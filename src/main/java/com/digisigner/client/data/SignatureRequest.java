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

    @XmlElement(name = "documents")
    private List<Document> documents = new ArrayList<>();

    public void addDocument(Document document) {
        documents.add(document);
    }

    public String getSignatureRequestId() {
        return signatureRequestId;
    }
}
