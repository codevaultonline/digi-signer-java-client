package com.digisigner.client.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The signature request class holds data to make signature request to sign documents.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignatureRequest {

    @XmlElement(name = "signature_request_id")
    private String signatureRequestId;

    /**
     * The parameter indicates that the email notifications will be sent.
     */
    @XmlElement(name = "send_emails")
    private Boolean sendEmails;

    /**
     * The embedded parameter indicates if the sign page has to be in embedded style mode.
     */
    private Boolean embedded;

    @XmlElement(name = "redirect_for_signing_to_url")
    private String redirectForSigningToUrl;

    @XmlElement(name = "redirect_after_signing_to_url")
    private String redirectAfterSigningToUrl;

    @XmlElement(name = "use_text_tags")
    private boolean useTextTags = false;

    @XmlElement(name = "hide_text_tags")
    private boolean hideTextTags = false;

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

    public Boolean getSendEmails() {
        return sendEmails;
    }

    public void setSendEmails(Boolean sendEmails) {
        this.sendEmails = sendEmails;
    }

    public String getRedirectForSigningToUrl() {
        return redirectForSigningToUrl;
    }

    public void setRedirectForSigningToUrl(String redirectForSigningToUrl) {
        this.redirectForSigningToUrl = redirectForSigningToUrl;
    }

    public String getRedirectAfterSigningToUrl() {
        return redirectAfterSigningToUrl;
    }

    public void setRedirectAfterSigningToUrl(String redirectAfterSigningToUrl) {
        this.redirectAfterSigningToUrl = redirectAfterSigningToUrl;
    }

    public Boolean getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Boolean embedded) {
        this.embedded = embedded;
    }

    public boolean isUseTextTags() {
        return useTextTags;
    }

    public void setUseTextTags(boolean useTextTags) {
        this.useTextTags = useTextTags;
    }

    public boolean isHideTextTags() {
        return hideTextTags;
    }

    public void setHideTextTags(boolean hideTextTags) {
        this.hideTextTags = hideTextTags;
    }

    public Boolean getCompleted() {
        return completed;
    }
}
