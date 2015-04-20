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

    /**
     * The parameter indicates that the email notifications will be sent.
     */
    @XmlElement(name = "send_emails")
    private Boolean sendEmails;

    @XmlElement(name = "is_completed")
    private Boolean completed;

    /**
     * The embedded parameter indicates if the sign page has to be in embedded style mode.
     */
    private Boolean embedded;

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

    public Boolean getSendEmails() {
        return sendEmails;
    }

    public void setSendEmails(Boolean sendEmails) {
        this.sendEmails = sendEmails;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public Boolean getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Boolean embedded) {
        this.embedded = embedded;
    }
}
